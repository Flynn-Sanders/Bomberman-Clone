import java.util.ArrayList;

public class Manager {

    private DLList<ServerThread> clients;
    private GameManager gameManager;

    private boolean gameStarted;

    private ArrayList<Integer> readyIDs;

    int readyCount;
    int requiredReady;

    public Manager() {
        clients = new DLList<>();
        gameManager = new GameManager(this);
        readyCount=0; requiredReady=0;
        readyIDs = new ArrayList<>();

    }

    public synchronized void addThread(ServerThread st) {
        clients.add(st);
        requiredReady++;
    }
    public synchronized void removeThread(ServerThread st) {
        clients.remove(st);
        readyIDs.remove(Integer.valueOf(st.playerID));
        requiredReady--; 
        if (gameManager != null) {
            gameManager.removePlayer(st.playerID);
        }
    }

    public synchronized void resetGame() {
        this.gameStarted = false;
        this.readyCount = 0;
        this.gameManager = new GameManager(this); 
        readyIDs.clear();
        
        for (ServerThread st : clients) {
            Player newP = this.gameManager.addPlayer(st.playerID);
            st.setPlayer(newP); 
        }
        broadcast("BACK_TO_LOBBY");
    }

    public void broadcast(String message) {
        for (ServerThread st : clients) {
            st.send(message);
        }
    }
    public synchronized void playerReady(int id) {
        if(readyIDs.contains(id)) return;
        readyCount++;
        readyIDs.add(id);
        System.out.println("Player " + id + " is ready. Total ready: " + readyCount+" / "+clients.size());
        if (readyCount >= requiredReady) {
            startGame();
        }
        
    }
    public void startGame() {
        if (gameStarted) return; 
        gameStarted = true;
        
        gameManager.initializeGame();
        String mapData = gameManager.getMap().toString();
        broadcast("START:" + mapData);
        
        new Thread(gameManager).start(); 
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public int numClients(){return clients.size(); }
}