public class GameManager implements Runnable {

    private MyHashMap<String, Player> players;
    private MyHashMap<String, Bomb> bombs;

    private MinHeap<Bomb> bombQueue;

    private TileMap map;

    private boolean running;

    public GameManager() {
        players = new MyHashMap<>();
        bombs = new MyHashMap<>();
        bombQueue = new MinHeap<>();
        map = new TileMap(13, 11);
        running = true;
    }

    public void addPlayer(String id) {
        Player p = new Player(id);
        players.put(id, p);
    }

    public void removePlayer(String id) {
        players.remove(id);
    }

    public void movePlayer(String id, int dx, int dy) {
        Player p = players.get(id);
        if (p != null && p.isAlive()) {
            p.move(dx, dy, map);
        }
    }

    public void placeBomb(String playerId) {
        Player p = players.get(playerId);
        if (p == null) return;

        int row = p.getRow();
        int col = p.getCol();

        String key = row + "," + col;

        if (!bombs.containsKey(key)) {
            Bomb b = new Bomb(row, col, playerId, System.currentTimeMillis());
            bombs.put(key, b);
            bombQueue.add(b);
        }
    }

    private void updateBombs() {
        long now = System.currentTimeMillis();

        while (!bombQueue.isEmpty() && bombQueue.peek().shouldExplode(now)) {
            Bomb b = bombQueue.poll();

            String key = b.getRow() + "," + b.getCol();
            bombs.remove(key);

            explode(b);
        }
    }

    private void explode(Bomb b) {
        // TODO: explosion logic (cross pattern)
        // damage players, destroy blocks, chain bombs
    }

    private void updatePlayers() {
        // optional: movement smoothing / status updates
    }

    private void checkWinCondition() {
        // count alive players
    }

    @Override
    public void run() {
        while (running) {
            updatePlayers();
            updateBombs();
            checkWinCondition();

            // TODO: send state to clients

            try {
                Thread.sleep(16); // ~60 FPS
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}