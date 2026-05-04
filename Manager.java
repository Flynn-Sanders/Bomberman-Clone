public class Manager {

    private DLList<ServerThread> clients;
    private GameManager gameManager;

    public Manager() {
        clients = new DLList<>();
        gameManager = new GameManager();

        new Thread(gameManager).start();
    }

    public void addThread(ServerThread st) {
        clients.add(st);
    }

    public void broadcast(String message) {
        for (ServerThread st : clients) {
            st.send(message);
        }
    }

    public GameManager getGameManager() {
        return gameManager;
    }
}