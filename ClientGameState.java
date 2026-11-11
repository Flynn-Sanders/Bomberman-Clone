public class ClientGameState {

    private MyHashMap<Integer, Player> players;
    private MyHashMap<String, Bomb> bombs;

    public ClientGameState() {
        players = new MyHashMap<>();
        bombs = new MyHashMap<>();
    }

    public void updateFromServer(String data) {
        players = new MyHashMap<>();
        bombs = new MyHashMap<>();
        //int[][] map = null;
        
        
        String[] entries = data.split(";");
        for (String entry : entries) {
            if (entry.isEmpty()) continue;
            String[] parts = entry.split(":");
            if (parts.length < 2) continue;
            
            String type = parts[0];
            String[] values = parts[1].split(",");
                
            if (type.equals("P") && values.length >= 3) {
                // P:id,row,col
                int id = Integer.parseInt(values[0]);
                int posX = Integer.parseInt(values[1]);
                int posY = Integer.parseInt(values[2]);
                
                Player p = new Player(id);
                p.setPosition(posX, posY); 
                players.put(id, p);
            }else if (type.equals("B") && values.length >= 3) {
                // B: row, col, timer
                int row = Integer.parseInt(values[0]);
                int col = Integer.parseInt(values[1]);
                long timer = Long.parseLong(values[2]);
                String key = row + "," + col;
                Bomb b = new Bomb(row, col, -1, timer);  // not needed on client 
                bombs.put(key, b);
            }
            
        }
    }

    public MyHashMap<Integer, Player> getPlayers() {
        return players;
    }

    public MyHashMap<String, Bomb> getBombs() {
        return bombs;
    }
}