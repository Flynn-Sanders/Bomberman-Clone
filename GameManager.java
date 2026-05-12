public class GameManager implements Runnable {

    private MyHashMap<String, Player> players;
    private MyHashMap<String, Bomb> bombs;

    private MinHeap<Bomb> bombQueue;

    private TileMap map;

    private boolean running;

    Manager mg;

    public GameManager(Manager manager) {
        players = new MyHashMap<>();
        bombs = new MyHashMap<>();
        bombQueue = new MinHeap<>();
        mg = manager;
        map = new TileMap(13, 11);
        running = true;

    }
    public TileMap getMap(){ return map; }

    public Player addPlayer(String id) {
        Player p = new Player(id);
        players.put(id, p);
        return p;
    }

    public void removePlayer(String id) {
        players.remove(id);
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
           public void initializeGame() {
        map = new TileMap(13, 11);
    
        if (players.size() > 0) {
            DLList<String> keys = players.keySet().toLinkedList();
            int[][] corners = {
                {0, 0}, // top left
                {(map.getCols() - 1) * TileMap.TILE_SIZE, 0}, // top right
                {0, (map.getRows() - 1) * TileMap.TILE_SIZE}, // bottom left
                {(map.getCols() - 1) * TileMap.TILE_SIZE, (map.getRows() - 1) * TileMap.TILE_SIZE} // bottom right
            };
    
            int i = 0;
            for (String id : keys) {
                Player p = players.get(id);
                if (p != null) {
                    int[] corner = corners[i % 4];
                    p.setPosition(corner[0], corner[1]); // Use pixel positions
                    i++;
                }
            }
        }
    }

    private void explode(Bomb b) {
        // damage players, destroy blocks, chain bombs, etc
    }

    private void updatePlayers() {
        DLList<String> playerKeys = players.keySet().toLinkedList();
        for (String id : playerKeys) {
            Player p = players.get(id);
            if (p != null && p.isAlive()) {
                p.update(map); 
            }
        }
    }

    private void checkWinCondition() {
        // count alive players 
    }

    private String getStateString() { 
        // format: P:id,row,col;B:row,col,timer;...
        StringBuilder sb = new StringBuilder();
        
        // P:id,row,col;
        DLList<String> playerKeys = players.keySet().toLinkedList();
        for (String id : playerKeys) {
            Player p = players.get(id);
            if (p != null) {
                sb.append("P:").append(id).append(",").append(p.getRow()).append(",").append(p.getCol()).append(";");
            }
        }
        
        // B:row,col,timer;
        DLList<String> bombKeys = bombs.keySet().toLinkedList();
        for (String key : bombKeys) {
            Bomb b = bombs.get(key);
            if (b != null) {
                sb.append("B:").append(b.getRow()).append(",").append(b.getCol()).append(",").append(b.getTimer()).append(";");
            }
        }
        if (sb.length() > 0 && sb.charAt(sb.length() - 1) == ';') {
            sb.deleteCharAt(sb.length() - 1);
        }
        
        return sb.toString();
    }

    @Override
    public void run() {
        while (running) {
            updatePlayers();
            updateBombs();
            checkWinCondition();

            mg.broadcast("STATE:"+getStateString());

            try {
                Thread.sleep(16); // ~60 FPS
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}