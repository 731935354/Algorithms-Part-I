import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation {
    public static void main(String[] args) {
        int n = 0;
        if (args.length == 1) n = Integer.parseInt(args[0]);
        RandomizedQueue<String> rq = new RandomizedQueue<>();
        String word;
        while (!StdIn.isEmpty()) {
            word = StdIn.readString();
            rq.enqueue(word);
        }
        for (int i = 0; i < n; i++)
            StdOut.println(rq.dequeue());
    }
}
