import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.*;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;


public class ClientScreen extends JPanel implements ActionListener, KeyListener {


    private ObjectOutputStream out;
    private ObjectInputStream in;

    private JButton readyButton;

    private String username;
    private int myPlayerID;

    private boolean[] keys = new boolean[256];

    private ClientGameState gameState;

    private boolean started;

    private int[][] map;


    public ClientScreen() {
        this.setLayout(null);
        gameState = new ClientGameState();
        started=false;

        readyButton = new JButton("READY");
        readyButton.setBounds((getWidth()-200)/2, 500, 200, 50);
        readyButton.setFont(new Font("Arial",Font.BOLD,30));
        readyButton.addActionListener(this);
        readyButton.setForeground(Color.green);
        readyButton.setBackground(Color.darkGray);
        this.add(readyButton);

        this.setFocusable(true);
        this.addKeyListener(this);
    }

    public Dimension getPreferredSize() {
        return new Dimension(1920, 1080);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        readyButton.setBounds((getWidth() - 200) / 2, 500, 200, 50);
        if(!started){
            g.setColor(Color.black);
            g.fillRect(0, 0, 1920, 1080);
            g.setColor(Color.red);
            Font f = new Font("Arial",Font.BOLD,90);//title
            g.setFont(f);
            FontMetrics fm = g.getFontMetrics();
            int width = fm.stringWidth("BOMBERMAN");
            int x = (getWidth()-width)/2;
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
            
            MyHashMap<Integer, Player> players = gameState.getPlayers();
            DLList<Integer> playerKeys = players.keySet().toLinkedList();
            int size = TileMap.TILE_SIZE;
            for (int id : playerKeys) {
            Player p = gameState.getPlayers().get(id);
                if (p != null) {
                    g.setColor((id==myPlayerID) ? Color.BLUE : Color.RED);
                    // DRAW PIXELS, NOT GRID COORDS
                    g.fillOval(p.getX(), p.getY(), size - 4, size - 4);
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
    private void playSound(String fileName) {
        try {
            File soundFile = new File("sounds/" + fileName+".wav"); 
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (Exception e) {
            System.err.println("Error playing sound: " + e.getMessage());
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        // only send once per press
        if (code < keys.length && !keys[code]) {
            keys[code] = true;
            sendMoveCommand("MOVE_START", code);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (code < keys.length) {
            keys[code] = false;
            sendMoveCommand("MOVE_STOP", code);
        }
    }

    
    private void sendMoveCommand(String action, int code) {
        String dir = "";
        if (code == KeyEvent.VK_W) dir = "UP";
        else if (code == KeyEvent.VK_S) dir = "DOWN";
        else if (code == KeyEvent.VK_A) dir = "LEFT";
        else if (code == KeyEvent.VK_D) dir = "RIGHT";
        else if (code == KeyEvent.VK_SPACE && action.equals("MOVE_START")) {
            try {
                out.writeObject("BOMB");
                out.flush();
            } catch (Exception ex) {}
            return;
        }

        if (!dir.isEmpty()) {
            try {
                out.writeObject(action + ":" + dir);
                out.flush();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void startGame() {
        remove(readyButton);   
        started=true;
        this.requestFocusInWindow();
        repaint();
    }

    public void connect() {
        new Thread(() -> { 
            try {
                Socket socket = new Socket("localhost", 1024);
                out = new ObjectOutputStream(socket.getOutputStream());
                out.flush();
                in = new ObjectInputStream(socket.getInputStream());

                username = JOptionPane.showInputDialog("Enter username:");
                out.writeObject("JOIN:" + username);
                out.flush();

                while (true) {
                    String msg = (String) in.readObject();
                    handleMessage(msg);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    

    private void handleMessage(String msg) {

        if (msg.startsWith("START")) {
            updateGridFromString(msg.substring(6));
            SwingUtilities.invokeLater(() -> {
                startGame();
            });
        }
        if (msg.startsWith("JOINED:")) {
            this.myPlayerID = Integer.parseInt(msg.split(":")[1]);
        }
        if (msg.startsWith("MAP_UPDATE:")) {
            String[] parts = msg.substring(11).split(",");
            if (parts.length == 3) {
                int r = Integer.parseInt(parts[0]);
                int c = Integer.parseInt(parts[1]);
                int val = Integer.parseInt(parts[2]);
                
                if (map != null && r < map.length && c < map[0].length) {
                    map[r][c] = val;
                    SwingUtilities.invokeLater(() -> repaint());
                }
            }
        }
        if(msg.startsWith("WINNER:")){
            String winner = msg.split(":")[1];
            String message = winner.equals("NONE") ? "It's a tie!" : "Player " + winner + " wins!";
            playSound("win");
            JOptionPane.showMessageDialog(this, message);
        }
        if (msg.equals("EXPLOSION")) {
            playSound("explosion");
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
    
    


