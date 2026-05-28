import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerThread extends Thread {

    private Socket socket;
    private Manager manager;

    private Player player;

    private ObjectOutputStream out;
    private ObjectInputStream in;
    String displayName; int playerID;

    public ServerThread(Socket socket, Manager manager, int ID) {
        this.socket = socket;
        this.manager = manager;
        this.playerID=ID;
        try {
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.out.flush();
            this.in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) { e.printStackTrace(); }
    }

    public Player getPlayer() {
        return player;
    }
    public int getPlayerID() {
        return playerID;
    }

    public void run() {
        try {
            while (!socket.isClosed()) {
                String msg = read();
                if (msg == null) break;
                handleMessage(msg);
            }
        } finally {
            manager.getGameManager().removePlayer(playerID);
            closeConnection();
        }
    }

    private void handleMessage(String msg) {
        if (msg == null || msg.isEmpty()) return;
        String[] parts = msg.split(":");
        if (parts.length == 0) return;

        String command = parts[0];

        GameManager gm = manager.getGameManager();

        switch (command) {
            case "JOIN":
                if (parts.length < 2) return;
                this.displayName = parts[1];
                this.player = manager.getGameManager().addPlayer(playerID);
                send("JOINED:" + playerID);
                break;
            case "READY":
                if (playerID >= 0) {
                    manager.playerReady(playerID);
                }
                break;
                case "MOVE_START":
                if (parts.length < 2) return;
                if (player != null) { 
                    switch (parts[1]) {
                        case "UP":
                            player.movingUp = true;
                            break;
                        case "DOWN":
                            player.movingDown = true;
                            break;
                        case "LEFT":
                            player.movingLeft = true;
                            break;
                        case "RIGHT":
                            player.movingRight = true;
                            break;
                    }
                }
                break;
            case "MOVE_STOP":
                if (parts.length < 2) return;
                if (player != null) { 
                    switch (parts[1]) {
                        case "UP":
                            player.movingUp = false;
                            break;
                        case "DOWN":
                            player.movingDown = false;
                            break;
                        case "LEFT":
                            player.movingLeft = false;
                            break;
                        case "RIGHT":
                            player.movingRight = false;
                            break;
                    }
                }
                break;
            case "BOMB":
                gm.placeBomb(playerID);
                break;
            default:
                break;
        }
    }

    public void send(String msg) {
        try {
            out.writeObject(msg);
            out.flush();
        } catch (IOException e) { e.printStackTrace(); }
    }

    private String read() {
        try {
            return (String) in.readObject();
        } catch (Exception e) {
            return null;
        }
    }

    private void closeConnection() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}