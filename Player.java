public class Player {

    private String id;

    private int x, y;
    private int speed;

    private int bombCount;
    private int bombRange;

    private boolean alive;

    public Player(String id) {
        this.id = id;
        this.x = 0;
        this.y = 0;
        this.speed = 4;
        this.bombCount = 1;
        this.bombRange = 1;
        this.alive = true;
    }

    public void move(int dx, int dy, TileMap map) {
        int newX = x + dx * speed;
        int newY = y + dy * speed;

        if (!map.isBlocked(newX, newY)) {
            x = newX;
            y = newY;
        }
    }

    public int getRow() {
        return y / TileMap.TILE_SIZE;
    }

    public int getCol() {
        return x / TileMap.TILE_SIZE;
    }

    public boolean isAlive() {
        return alive;
    }

    public void kill() {
        alive = false;
    }

    public String getId() {
        return id;
    }
}