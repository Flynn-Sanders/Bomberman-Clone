import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.*;
import javax.swing.*;


public class ClientScreen extends JPanel implements ActionListener, KeyListener {


    private ObjectOutputStream out;
    private ObjectInputStream in;

    private JButton readyButton;

    private String username;
    private String myPlayerID;

    private ClientGameState gameState;

    private boolean started;

    private int[][] map;


    public ClientScreen() {
        this.setLayout(null);
        gameState = new ClientGameState();
        started=false;

        readyButton = new JButton("Ready");
        readyButton.setBounds(300, 620, 100, 30);
        readyButton.addActionListener(this);
        this.add(readyButton);

        this.setFocusable(true);
    }

    public Dimension getPreferredSize() {
        return new Dimension(1920, 1080);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(!started){
            g.setColor(Color.black);
            g.fillRect(0, 0, 1920, 1080);
            g.setColor(Color.red);
            Font f = new Font("Arial",Font.BOLD,40);//title
            g.setFont(f);
            FontMetrics fm = g.getFontMetrics();
            int width = fm.stringWidth("BOMBERMAN");
            int x = (WIDTH-width)/2;
            g.drawString("BOMBERMAN", x, 300);
        }
        else{
            if (map != null) {
                int size = TileMap.TILE_SIZE;
                for (int r = 0; r < map.length; r++) {
                    for (int c = 0; c < map[r].length; c++) {
                        g.setColor(switch (map[r][c]) {
                            case 0 -> Color.white;
                            case 1 -> Color.darkGray;
                            case 2 -> Color.lightGray;
                            default -> Color.pink; 
                            // 0 = empty, 1 = indestructible wall, 2 = destructible wall
                            //will use images later
                        });
                        g.fillRect(c * size, r * size, size, size);
                    }
                }
            }
            
            MyHashMap<String, Player> players = gameState.getPlayers();
            DLList<String> playerKeys = players.keySet().toLinkedList();
            for (String id : playerKeys) {
                Player p = players.get(id); 
                if (p != null) {
                    g.setColor(Color.blue);  // change to sprite later
                    g.fillOval(p.getCol() * TileMap.TILE_SIZE, p.getRow() * TileMap.TILE_SIZE, TileMap.TILE_SIZE, TileMap.TILE_SIZE);
                }
            }
            
            MyHashMap<String, Bomb> bombs = gameState.getBombs();
            DLList<String> bombKeys = bombs.keySet().toLinkedList();
            for (String key : bombKeys) {
                Bomb b = bombs.get(key);
                if (b != null) {
                    g.setColor(Color.red);  // also change to sprite later
                    g.fillOval(b.getCol() * TileMap.TILE_SIZE, b.getRow() * TileMap.TILE_SIZE, TileMap.TILE_SIZE, TileMap.TILE_SIZE);
                }
            }
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == readyButton) {
            try {
                out.writeObject("READY");
                out.flush();
                readyButton.setEnabled(false); 
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    private void sendMessage(String str){
        try {
            out.writeObject(str);
            out.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()){
            case KeyEvent.VK_W:
                sendMessage("PRESS:W");
                break;
            case KeyEvent.VK_A:
                sendMessage("PRESS:A"); 
                break; 
            case KeyEvent.VK_S:
                    sendMessage("PRESS:S");
                    break;
            case KeyEvent.VK_D:
                    sendMessage("PRESS:D"); 
                    break;
            default:
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch(e.getKeyCode()){
            case KeyEvent.VK_W:
                sendMessage("RELEASE:W");
                break;
            case KeyEvent.VK_A:
                sendMessage("RELEASE:A"); 
                break; 
            case KeyEvent.VK_S:
                    sendMessage("RELEASE:S");
                    break;
            case KeyEvent.VK_D:
                    sendMessage("RELEASE:D"); 
                    break;
            default:
                break;
        }
    }

    public void startGame() {
        remove(readyButton);   
        started=true;
        repaint();
    }

    public void connect() {
        try {
            Socket socket = new Socket("localhost", 1024);
            
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(socket.getInputStream());

            System.out.println("Connected to the chat server!");

            username = JOptionPane.showInputDialog("Enter username:");
            out.writeObject("JOIN:" + username);
            out.flush(); 

            while (true) {
            try {
                String msg = (String) in.readObject();
                handleMessage(msg);
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
            
        } catch (Exception e) {
            System.out.println("Connection error: " + e.getMessage());
        }
    }
    private void handleMessage(String msg) {

        System.out.println("Received: " + msg);

        if (msg.startsWith("START")) {
            updateGridFromString(msg.substring(6));
            SwingUtilities.invokeLater(() -> {
                startGame();
            });
        }
        if (msg.startsWith("JOINED:")) {
            this.myPlayerID = msg.split(":")[1];
        }
        if (msg.startsWith("STATE:")) { 
            gameState.updateFromServer(msg.substring(6));
            SwingUtilities.invokeLater(() -> {
                repaint();  
            });
        }

    }
    public void updateGridFromString(String data) {
        String[] rows = data.split(";");
        int effectiveRows = rows.length;
        if (effectiveRows > 0 && rows[effectiveRows - 1].isEmpty()) {
            effectiveRows--;
        }
        if (effectiveRows == 0) {
            return;
        }

        String[] firstCol = rows[0].split(",");
        this.map = new int[effectiveRows][firstCol.length];
        for (int r = 0; r < effectiveRows; r++) {
            String[] cols = rows[r].split(",");
            for (int c = 0; c < cols.length && c < firstCol.length; c++) {
                map[r][c] = Integer.parseInt(cols[c]);
            }
        }
    }

    
}
    
    


