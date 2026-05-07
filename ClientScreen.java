import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import javax.swing.*;


public class ClientScreen extends JPanel implements ActionListener {


    private ObjectOutputStream out;
    private ObjectInputStream in;

    private JButton readyButton;

    private String username;
    private String myPlayerID;

    private ClientGameState gameState;

    private int[][] map;


    public ClientScreen() {
        this.setLayout(null);
        gameState = new ClientGameState();

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
                g.fillRect(p.getCol() * TileMap.TILE_SIZE, p.getRow() * TileMap.TILE_SIZE, TileMap.TILE_SIZE, TileMap.TILE_SIZE);
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
    public void startGame() {
        remove(readyButton);   
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
    
    


