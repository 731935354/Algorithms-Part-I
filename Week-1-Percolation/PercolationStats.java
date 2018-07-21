import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdOut;

public class PercolationStats {
    private static final double CONFIDENCE_CONST = 1.96;

    private final double[] fractions;
    private final int t; // number of experiments

    public PercolationStats(int n, int trials) {   // perform trials independent experiments on an n-by-n grid
        if (n <= 0 || trials <= 0)
            throw new IllegalArgumentException("n and trials must be greater " +
                    "than 0 at the same time.");
        int[] notOpen; // for randomly open among blocked nodes
        int[] pos;
        fractions = new double[trials];
        t = trials;
        int threshold;
        for (int i = 0; i < trials; i++) {
//            StdOut.println(i);
            notOpen = new int[n * n];
            for (int j = 0; j < n * n; j++)
                notOpen[j] = j;
            Percolation perc = new Percolation(n);
            while (!perc.percolates()) {
                pos = randomPosAmongBlocked(notOpen, n, perc.numberOfOpenSites());
                // swap opened node and not opened tailing node in notOpen
                int tmp = notOpen[n * n - 1 - perc.numberOfOpenSites()];
                notOpen[n * n - 1 - perc.numberOfOpenSites()] = notOpen[pos[0] * n + pos[1]];
                notOpen[pos[0] * n + pos[1]] = tmp;
                perc.open(pos[0] + 1, pos[1] + 1);
            }
            threshold = perc.numberOfOpenSites();
//            System.out.println(threshold);
            double thresholdD = threshold;
            fractions[i] = thresholdD / (n * n);
        }
    }

    private int[] randomPosAmongBlocked(int[] notOpen, int sideLen, int offsetFromTail) {
        int randomPosOfNotOpen = StdRandom.uniform(sideLen * sideLen - offsetFromTail);
        int numInPosOfNotOpen = notOpen[randomPosOfNotOpen];
        int row = numInPosOfNotOpen / sideLen;
        int col = numInPosOfNotOpen % sideLen;
        int[] pos = {row, col};
        return pos;
    }

    public double mean() { // sample mean of percolation threshold
        return StdStats.mean(fractions);
    }

    public double stddev() { // sample standard deviation of percolation threshold
        return StdStats.stddev(fractions);
    }

    public double confidenceLo() { // low  endpoint of 95% confidence interval
        return mean() - CONFIDENCE_CONST * stddev() / Math.sqrt(t);
    }

    public double confidenceHi() { // high endpoint of 95% confidence interval
        return mean() + CONFIDENCE_CONST * stddev() / Math.sqrt(t);
    }

    public static void main(String[] args) { // test client
        if (args.length == 2) {
            int n = Integer.parseInt(args[0]);
            int t = Integer.parseInt(args[1]);
            PercolationStats ps = new PercolationStats(n, t);
            StdOut.println("mean                    = " + ps.mean());
            StdOut.println("stddev                  = " + ps.stddev());
            StdOut.println("95% confidence interval = [" + ps.confidenceLo() + "," + ps.confidenceHi() + "]");
        }
//        PercolationStats ps = new PercolationStats(200, 30);
//        StdOut.println("mean                    = " + ps.mean());
//        StdOut.println("stddev                  = " + ps.stddev());
//        StdOut.println("95% confidence interval = [" + ps.confidenceLo() + "," + ps.confidenceHi() + "]");
    }
}
