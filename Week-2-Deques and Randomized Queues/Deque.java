import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdOut;

public class Deque<Item> implements Iterable<Item> {
    private Node first;
    private Node last;
    private int size;

    private class Node {
        Item item;
        Node next; // reference to the next node
        Node prev; // reference to the previous node
    }

    public Deque() { // construct an empty deque
        first = null;
        last = null;
        size = 0;
    }

    public boolean isEmpty() {                // is the deque empty?
        return first == null || last == null;
    }

    public int size() {                       // return the number of items on the deque
        return size;
    }

    public void addFirst(Item item) {         // add the item to the front
        if (item == null)
            throw new IllegalArgumentException("parameter can not be null.");
        Node oldFirst = first;
        first = new Node();
        first.item = item;
        first.next = oldFirst;
        first.prev = null;
        if (oldFirst != null)
            oldFirst.prev = first;
        size++;
        if (size == 1)
            last = first;
    }

    public void addLast(Item item) {          // add the item to the end
        if (item == null)
            throw new IllegalArgumentException("parameter can not be null.");
        Node oldLast = last;
        last = new Node();
        last.item = item;
        last.prev = oldLast;
        last.next = null;
        if (oldLast != null)
            oldLast.next = last;
        size++;
        if (size == 1)
            first = last;
    }

    public Item removeFirst() {               // remove and return the item from the front
        if (isEmpty())
            throw new NoSuchElementException("empty deque.");
        Node tmp = first;
        first = first.next;
        if (first != null)
            first.prev = null;
        size--;
        return tmp.item;
    }

    public Item removeLast() {                // remove and return the item from the end
        if (isEmpty())
            throw new NoSuchElementException("empty deque.");
        Node tmp = last;
        last = last.prev;
        if (last != null)
            last.next = null;
        size--;
        return tmp.item;
    }

    public Iterator<Item> iterator() {        // return an iterator over items in order from front to end
        return new ListIterator();
    }

    private class ListIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext() {
            return current != null;
        }

        public Item next() {
            if (isEmpty())
                throw new NoSuchElementException("empty deque.");
            Item item = current.item;
            current = current.next;
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException("remove not supported.");
        }
    }

    public static void main(String[] args) {  // unit testing (optional)
        Deque<Integer> deque = new Deque<>();
        deque.addFirst(0);
        deque.addFirst(1);
        deque.removeLast();
        deque.addFirst(3);
        deque.addFirst(4);
        deque.addLast(5);
        deque.isEmpty();
        deque.size();
        StdOut.println(deque.removeLast());
    }
}
