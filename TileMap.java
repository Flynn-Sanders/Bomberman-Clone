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
                if (r % 2 == 1 && c % 2 == 1) {
                    grid[r][c] = 1; // solid walls in repeating pattern
                } else if( (r==0 || r==rows-1) && (c==0 || c==cols-1) ){
                    grid[r][c]=0; //empty at 4 corners for players
                } else {
                    grid[r][c] = Math.random() < 0.3 ? 2 : 0; // 33% chance breakable on all other squares
                }
            }
        }
    }

    public boolean isBlocked(int x, int y) {
        int col = x / TILE_SIZE;
        int row = y / TILE_SIZE;

        if (row < 0 || col < 0 || row >= rows || col >= cols)
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