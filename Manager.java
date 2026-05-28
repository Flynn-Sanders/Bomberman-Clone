public class Manager {

    private DLList<ServerThread> clients;
    private GameManager gameManager;

    private boolean gameStarted;

    int readyCount;
    int requiredReady;

    public Manager() {
        clients = new DLList<>();
        gameManager = new GameManager(this);
        readyCount=0; requiredReady=0;

    }

    public synchronized void addThread(ServerThread st) {
        clients.add(st);
        requiredReady++;
    }
    public void removeThread(ServerThread st) {
        clients.remove(st);
        requiredReady--;
    }
    public synchronized void resetGame() {
    this.gameStarted = false;
    this.readyCount = 0;
    this.gameManager = new GameManager(this); 
    for (ServerThread st : clients) {
        if (st.getPlayer() != null) {
            this.gameManager.addPlayer(st.getPlayerID());
        }
    }
    broadcast("BACK_TO_LOBBY");
}

    public void broadcast(String message) {
        for (ServerThread st : clients) {
            st.send(message);
        }
    }
    public synchronized void playerReady(int id) {
        readyCount++;
        System.out.println("Ready: "+readyCount+" / "+clients.size());
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