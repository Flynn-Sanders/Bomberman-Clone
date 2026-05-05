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
        while (true) {
            String msg = read();

            handleMessage(msg);
        }
    }

    private void handleMessage(String msg) {
        String[] parts = msg.split(":");

        String command = parts[0];

        GameManager gm = manager.getGameManager();

        switch (command) {
            case "JOIN":
                this.displayName = parts[1];
                this.playerID = displayName + "#" + id; // unique id
                
                manager.getGameManager().addPlayer(playerID); 
                
                send("JOINED:" + playerID); 
                break;
            case "READY":
                manager.playerReady(playerID);
                break;
            case "MOVE":
                int dx = Integer.parseInt(parts[1]);
                int dy = Integer.parseInt(parts[2]);
                gm.movePlayer(playerID, dx, dy);
                break;

            case "BOMB":
                gm.placeBomb(playerID);
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
        } catch (Exception e) { return ""; }
    }
}