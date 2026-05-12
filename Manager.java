public class Manager {

    private DLList<ServerThread> clients;
    private GameManager gameManager;

    int readyCount;
    int requiredReady;

    public Manager() {
        clients = new DLList<>();
        gameManager = new GameManager(this);
        readyCount=0; requiredReady=0;

        new Thread(gameManager).start();
    }

    public synchronized void addThread(ServerThread st) {
        clients.add(st);
        requiredReady++;
    }

    public void broadcast(String message) {
        for (ServerThread st : clients) {
            st.send(message);
        }
    }
    public synchronized void playerReady(String id) {
        readyCount++;
        System.out.println("Ready: "+readyCount+" / "+clients.size());
        if (readyCount >= requiredReady) {
            startGame();
        }
        
    }
    public void startGame() {
        gameManager.initializeGame();
        String mapData= gameManager.getMap().toString();
        broadcast("START:"+mapData);
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public int numClients(){return clients.size(); }
}