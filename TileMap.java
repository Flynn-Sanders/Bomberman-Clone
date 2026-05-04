public class TileMap {

    public static final int TILE_SIZE = 40;

    private int rows, cols;
    private int[][] grid;

    public TileMap(int cols, int rows) {
        this.cols = cols;
        this.rows = rows;
        grid = new int[rows][cols];

        generateMap();
    }

    private void generateMap() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (r % 2 == 1 && c % 2 == 1) {
                    grid[r][c] = 1; // solid wall
                } else {
                    grid[r][c] = Math.random() < 0.3 ? 2 : 0; // 1/3 chance breakable otherwise nothing
                }
            }
        }
    }

    public boolean isBlocked(int x, int y) {
        int col = x / TILE_SIZE;
        int row = y / TILE_SIZE;

        if (row < 0 || col < 0 || row >= rows || col >= cols)
            return true;

        return grid[row][col] == 1;
    }

    public void destroyBlock(int row, int col) {
        if (grid[row][col] == 2) {
            grid[row][col] = 0;
        }
    }
}