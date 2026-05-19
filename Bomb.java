public class Bomb implements Comparable<Bomb> {

    private int row, col;
    private int ownerId;

    private long placedTime;
    private long delay = 2000; // 2 seconds

    private int range;

    public Bomb(int row, int col, int ownerId, long time) {
        this.row = row;
        this.col = col;
        this.ownerId = ownerId;
        this.placedTime = time;
        this.range = 1;
    }

    public boolean shouldExplode(long currentTime) {
        return currentTime - placedTime >= delay;
    }

    public int getRow() { return row; }
    public int getCol() { return col; }
    public long getTimer() { return placedTime; }
    public int getRange() { return range; }

    @Override
    public int compareTo(Bomb other) {
        return Long.compare(this.placedTime + delay, other.placedTime + other.delay);
    }
}