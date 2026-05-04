public class ClientGameState {

    private MyHashMap<String, Player> players;
    private MyHashMap<String, Bomb> bombs;

    public ClientGameState() {
        players = new MyHashMap<>();
        bombs = new MyHashMap<>();
    }

    public void updateFromServer(String data) {
        // get server message and update state
    }

    public MyHashMap<String, Player> getPlayers() {
        return players;
    }
}