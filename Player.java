public class Player {

    private int id;

    private int x, y;
    private int speed;

    private int bombCount;
    private int bombRange;

    private boolean alive;

    public boolean movingUp, movingDown, movingLeft, movingRight;

    public Player(int id) {
        this.id = id;
        this.x = 0;
        this.y = 0;
        this.speed = 4;
        this.bombCount = 1;
        this.bombRange = 1;
        this.alive = true;
    }

    public void update(TileMap map, MyHashMap<String, Bomb> bombs) {
        int dx = 0;
        int dy = 0;

        if (movingUp && !movingDown) dy = -1;
        else if (movingDown && !movingUp) dy = 1;

        if (movingLeft && !movingRight) dx = -1;
        else if (movingRight && !movingLeft) dx = 1;

        if (dx != 0 || dy != 0) {
            move(dx, dy, map, bombs);
        }
    }
    
    public void move(int dx, int dy, TileMap map, MyHashMap<String, Bomb> bombs) {
        int nextX = x + dx * speed;
        int nextY = y + dy * speed;
        //current
        int currentRow = getRow();
        int currentCol = getCol();
        String currentTileKey = currentRow + "," + currentCol;
        //target
        int nextRow = (nextY + TileMap.TILE_SIZE / 2) / TileMap.TILE_SIZE;
        int nextCol = (nextX + TileMap.TILE_SIZE / 2) / TileMap.TILE_SIZE;
        String nextTileKey = nextRow + "," + nextCol;

        boolean isMapBlocked = map.isAreaBlocked(nextX, nextY, TileMap.TILE_SIZE - 4, TileMap.TILE_SIZE - 4);
        boolean isBombBlocked = bombs.containsKey(nextTileKey);

        if (isBombBlocked && nextTileKey.equals(currentTileKey)) {
            isBombBlocked = false; 
        }

        if (!isMapBlocked && !isBombBlocked) {
            x = nextX;
            y = nextY;
        }
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getRow() {
        return (y + TileMap.TILE_SIZE / 2) / TileMap.TILE_SIZE;
    }

    public int getCol() {
        return (x + TileMap.TILE_SIZE / 2) / TileMap.TILE_SIZE;
    }
    public int getX(){return x;}
    public int getY(){return y;}

    public boolean isAlive() {
        return alive;
    }

    public void kill() {
        alive = false;
    }

    public int getId() {
        return id;
    }
}