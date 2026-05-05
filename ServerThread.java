import java.net.Socket;

public class ServerThread extends Thread {

    private Socket socket;
    private Manager manager;

    public ServerThread(Socket socket, Manager manager) {
        this.socket = socket;
        this.manager = manager;
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
        String playerId = parts[1];

        GameManager gm = manager.getGameManager();

        switch (command) {
            case "READY":
                manager.playerReady(playerId);
            case "MOVE":
                int dx = Integer.parseInt(parts[2]);
                int dy = Integer.parseInt(parts[3]);
                gm.movePlayer(playerId, dx, dy);
                break;

            case "BOMB":
                gm.placeBomb(playerId);
                break;
        }
    }

    public void send(String msg) {
        // output stream send
    }

    private String read() {
        // input stream read
        return "";
    }
}