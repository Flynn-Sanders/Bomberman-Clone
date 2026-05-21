public class TileMap {

    public static final int TILE_SIZE = 65;

    private int rows, cols;
    private int[][] grid;

    public TileMap(int cols, int rows) {
        this.cols = cols;
        this.rows = rows;
        grid = new int[rows][cols];

        generateMap();
    }

    private void generateMap() { //0 empty, 1 solid wall, 2 breakable
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                // walls
                if (r % 2 == 1 && c % 2 == 1) {
                    grid[r][c] = 1; 
                } 
                // safe zones near spawns
                else if (isSafeZone(r, c)) {
                    grid[r][c] = 0; 
                } 
                // random breakables
                else {
                    grid[r][c] = Math.random() < 0.5 ? 2 : 0; 
                }
            }
        }
    }

    private boolean isSafeZone(int r, int c) {
        // top left + adjacent
        if ((r == 0 && c == 0) || (r == 0 && c == 1) || (r == 1 && c == 0)) return true;
        
        // top right + adjacent
        if ((r == 0 && c == cols-1) || (r == 0 && c == cols-2) || (r == 1 && c == cols-1)) return true;
        
        // bottom left + adjacent
        if ((r == rows-1 && c == 0) || (r == rows-1 && c == 1) || (r == rows-2 && c == 0)) return true;
        
        // bottom right + adjacent
        if ((r == rows-1 && c == cols-1) || (r == rows-1 && c == cols-2) || (r == rows-2 && c == cols-1)) return true;

        return false;
    }

    public boolean isBlocked(int x, int y) {
        int col = x / TILE_SIZE;
        int row = y / TILE_SIZE;

        if (row < 0 || col < 0 || row >= rows || col >= cols || x < 0 || y < 0)
            return true;

        return grid[row][col] != 0;
    }
    public boolean isAreaBlocked(int x, int y, int width, int height) {
        return isBlocked(x, y) || 
               isBlocked(x + width - 1, y) || 
               isBlocked(x, y + height - 1) || 
               isBlocked(x + width - 1, y + height - 1);
    }

    public void destroyBlock(int row, int col) {
        if (grid[row][col] == 2) {
            grid[row][col] = 0;
        }
    }
    public int getTileType(int row, int col) {
        if (isInBounds(row, col)) {
            return grid[row][col];
        }
        return -1; 
    }

    public boolean isInBounds(int row, int col) {
        return row >= 0 && col >= 0 && row < rows && col < cols;
    }

    public int getRows(){return rows;}
    public int getCols(){ return cols; }

    @Override
    public String toString(){
        StringBuilder text = new StringBuilder();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                text.append(grid[r][c]).append(",");
            }
            if (r < rows - 1) {
                text.append(";");
            }
        }
        return text.toString();
    }
}