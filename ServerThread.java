import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerThread extends Thread {

    private Socket socket;
    private Manager manager;

    private ObjectOutputStream out;
    private ObjectInputStream in;

    int id; String displayName; String playerID;

    public ServerThread(Socket socket, Manager manager, int ID) {
        this.socket = socket;
        this.manager = manager;
        id=ID;
        playerID="";
        try {
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.out.flush();
            this.in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) { e.printStackTrace(); }
    }

    public void run() {
        while (!socket.isClosed() && !Thread.currentThread().isInterrupted()) {
            String msg = read();
            if (msg == null || msg.isEmpty()) {
                break;
            }
            handleMessage(msg);
        }
        closeConnection();
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
                this.playerID = displayName + "#" + id; // unique id
                manager.getGameManager().addPlayer(playerID);
                send("JOINED:" + playerID);
                break;
            case "READY":
                if (playerID != null && !playerID.isEmpty()) {
                    manager.playerReady(playerID);
                }
                break;
            case "MOVE":
                if (parts.length < 3) return;
                try {
                    int dx = Integer.parseInt(parts[1]);
                    int dy = Integer.parseInt(parts[2]);
                    gm.movePlayer(playerID, dx, dy);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
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