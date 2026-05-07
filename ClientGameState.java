public class ClientGameState {

    private MyHashMap<String, Player> players;
    private MyHashMap<String, Bomb> bombs;

    public ClientGameState() {
        players = new MyHashMap<>();
        bombs = new MyHashMap<>();
    }

    public void updateFromServer(String data) {
        players = new MyHashMap<>();
        bombs = new MyHashMap<>();
        
        // P:id,row,col;B:row,col,timer;
        String[] entries = data.split(";");
        for (String entry : entries) {
            if (entry.isEmpty()) continue;
            String[] parts = entry.split(":");
            if (parts.length < 2) continue;
            
            String type = parts[0];
            String[] values = parts[1].split(",");
            
            if (type.equals("P") && values.length >= 3) {
                // Player: id, row, col
                String id = values[0];
                int row = Integer.parseInt(values[1]);
                int col = Integer.parseInt(values[2]);
                Player p = new Player(id);
                p.setPosition(col * TileMap.TILE_SIZE, row * TileMap.TILE_SIZE);
                players.put(id, p);
            } else if (type.equals("B") && values.length >= 3) {
                // B: row, col, timer
                int row = Integer.parseInt(values[0]);
                int col = Integer.parseInt(values[1]);
                long timer = Long.parseLong(values[2]);
                String key = row + "," + col;
                Bomb b = new Bomb(row, col, "", timer);  // not needed on client 
                bombs.put(key, b);
            }
        }
    }

    public MyHashMap<String, Player> getPlayers() {
        return players;
    }

    public MyHashMap<String, Bomb> getBombs() {
        return bombs;
    }
}