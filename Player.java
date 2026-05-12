public class Player {

    private String id;

    private int x, y;
    private int speed;

    private int bombCount;
    private int bombRange;

    private boolean alive;

    public boolean movingUp, movingDown, movingLeft, movingRight;

    public Player(String id) {
        this.id = id;
        this.x = 0;
        this.y = 0;
        this.speed = 4;
        this.bombCount = 1;
        this.bombRange = 1;
        this.alive = true;
    }

        public void update(TileMap map) {
        System.out.println("Updating player " + id + " at (" + x + ", " + y + ")"); // Debug log
        if (movingUp) move(0, -1, map);
        if (movingDown) move(0, 1, map);
        if (movingLeft) move(-1, 0, map);
        if (movingRight) move(1, 0, map);
    }
    
    public void move(int dx, int dy, TileMap map) {
        int nextX = x + dx * speed;
        int nextY = y + dy * speed;
    
        System.out.println("Attempting to move to (" + nextX + ", " + nextY + ")"); // Debug log
    
        int playerSize = TileMap.TILE_SIZE - 10;
        int offset = 5;
    
        if (!map.isAreaBlocked(nextX + offset, nextY + offset, playerSize, playerSize)) {
            x = nextX;
            y = nextY;
            System.out.println("Moved to (" + x + ", " + y + ")"); // Debug log
        } else {
            System.out.println("Blocked at (" + nextX + ", " + nextY + ")"); // Debug log
        }
    }
    public void setPosition(int x1, int y1){
        x=x1; y=y1;
    }

    public int getRow() {
        return y / TileMap.TILE_SIZE;
    }

    public int getCol() {
        return x / TileMap.TILE_SIZE;
    }
    public int getX(){return x;}
    public int getY(){return y;}

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