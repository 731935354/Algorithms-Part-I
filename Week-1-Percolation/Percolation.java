import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[][] openStat;
    private boolean[][] fullStat;
    private final WeightedQuickUnionUF wqu;
    private final int sideLen;
    private int numberOfOpenSites = 0;
    private int[] queue; // for bfsFill

    public Percolation(int n) { // create n-by-n grid, with all sites blocked
        if (n <= 0)
            throw new IllegalArgumentException("n must be no less than zero.");
        sideLen = n;
        openStat = new boolean[n][n];
        fullStat = new boolean[n][n];
        // initialize openStat and fullStat
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                openStat[i][j] = false;
                fullStat[i][j] = false;
            }
        }
        // first and last node are virtual top and bottom nodes respectively.
        wqu = new WeightedQuickUnionUF(n * n + 2);
        // initialize queue for bfsFill
        queue = new int[n * n];
        for (int i = 0; i < n * n; i++)
            queue[i] = -1;
    }

    private void validateParam(int param, int lowerLimit, int upperLimit) {
        if (param > upperLimit || param < lowerLimit) {
            throw new IllegalArgumentException("param must be in range [" + lowerLimit +
            "," + upperLimit + "]. current " + param);
        }
    }

    private void bfsFill(int row, int col) {
        row = row - 1;
        col = col - 1;
        int head = 0, tail = 0;
        int[] xDir = {0, 0, -1, 1};
        int[] yDir = {-1, 1, 0, 0};
        boolean[][] done = new boolean[sideLen][sideLen];
        for (int i = 0; i < sideLen; i++)
            for (int j = 0; j < sideLen; j++)
                done[i][j] = false;
        int x, y, newX, newY;
        queue[0] = row * sideLen + col;
        while (head <= tail) {
            int tmp = queue[head++];
            x = tmp / sideLen;
            y = tmp % sideLen;
            fullStat[x][y] = true;
            done[x][y] = true;
            for (int i = 0; i < xDir.length; i++) {
                newX = x + xDir[i];
                newY = y + yDir[i];
                validateParam(row, 0, sideLen - 1);
                validateParam(col, 0, sideLen - 1);
                if (newX >= 0 && newX < sideLen && newY >= 0 && newY < sideLen) {
                    if (!isFull(newX + 1, newY + 1)
                            && isOpen(newX + 1, newY + 1)
                            && !done[newX][newY]) {
                        queue[++tail] = newX * sideLen + newY;
                        done[newX][newY] = true;
                    }
                }
            }
        }
    }

    public void open(int row, int col) { // open site (row, col) if it is not open already
        row = row - 1;
        col = col - 1;
        validateParam(row, 0, sideLen - 1);
        validateParam(col, 0, sideLen - 1);
        // if open already, do nothing.
        if (isOpen(row + 1, col + 1))
            return;
        // update openStat
        openStat[row][col] = true;
        numberOfOpenSites++;

        int curNode = row * sideLen + col + 1;

        // union site in the first row to the virtual top site
        if (row == 0) {
            wqu.union(0, curNode);
            bfsFill(row + 1, col + 1);
        }
        // union site in the bottom row to the virtual bottom site.
        if (row == sideLen - 1)
            wqu.union(curNode, sideLen * sideLen + 1);
        // union current site to its neighbors and fill sites as many as possible.
        if (row > 0) { // upper neighbor
            if (isOpen(row - 1 + 1, col + 1))
                wqu.union(curNode, curNode - sideLen);
            if (isFull(row - 1 + 1, col + 1))
                bfsFill(row + 1, col + 1);
        }
        if (row < sideLen - 1) { // lower neighbor
            if (isOpen(row + 1 + 1, col + 1))
                wqu.union(curNode, curNode + sideLen);
            if (isFull(row + 1 + 1, col + 1))
                bfsFill(row + 1, col + 1);
        }
        if (col > 0) { // left neighbor
            if (isOpen(row + 1, col - 1 + 1))
                wqu.union(curNode, curNode - 1);
            if (isFull(row + 1, col - 1 + 1))
                bfsFill(row + 1, col + 1);
        }
        if (col < sideLen - 1) { // right neighbor
            if (isOpen(row + 1, col + 1 + 1))
                wqu.union(curNode, curNode + 1);
            if (isFull(row + 1, col + 1 + 1))
                bfsFill(row + 1, col + 1);
        }
    }

    public boolean isOpen(int row, int col) { // is site (row, col) open?
        validateParam(row, 1, sideLen);
        validateParam(col, 1, sideLen);
        return openStat[row-1][col-1];
    }

    public boolean isFull(int row, int col) { // is site (row, col) full?
        validateParam(row, 1, sideLen);
        validateParam(col, 1, sideLen);
        return fullStat[row-1][col-1];
    }

    public int numberOfOpenSites() { // number of open sites
        return numberOfOpenSites;
    }

    public boolean percolates() { // does the system percolate?
        return wqu.connected(0, sideLen * sideLen + 1);
    }

    public static void main(String[] args) {
        Percolation p = new Percolation(5);
        p.open(1, 1);
        p.open(2, 1);
        p.open(3, 1);
        p.open(4, 1);
        p.open(5, 1);
        System.out.println(p.percolates());
        System.out.println(p.numberOfOpenSites());
    }
}
