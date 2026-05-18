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

        public void update(TileMap map) {
        System.out.println("Updating player " + id + " at (" + x + ", " + y + ")"); // Debug log
        int dx = 0;
        int dy = 0;

        if (movingUp && !movingDown) dy = -1;
        else if (movingDown && !movingUp) dy = 1;

        if (movingLeft && !movingRight) dx = -1;
        else if (movingRight && !movingLeft) dx = 1;

        if (dx != 0 || dy != 0) {
            move(dx, dy, map);
        }
    }
    
    public void move(int dx, int dy, TileMap map) {
        int nextX = x + dx * speed;
        int nextY = y + dy * speed;
    
        System.out.println("Attempting to move to (" + nextX + ", " + nextY + ")"); // Debug log
    
        int playerSize = TileMap.TILE_SIZE - 4;
        int mapWidth = map.getCols() * TileMap.TILE_SIZE;
        int mapHeight = map.getRows() * TileMap.TILE_SIZE;

        if (nextX < 0 || nextY < 0 || nextX + playerSize > mapWidth || nextY + playerSize > mapHeight) {
            System.out.println("Blocked by boundary at (" + nextX + ", " + nextY + ")");
            return;
        }
    
        if (!map.isAreaBlocked(nextX, nextY, playerSize, playerSize)) {
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