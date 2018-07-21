import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] items;
    private int nItems = 0;

    public RandomizedQueue() {                // construct an empty randomized queue
        items = (Item[]) new Object[1];
    }

    public boolean isEmpty() {                 // is the randomized queue empty?
        return nItems == 0;
    }

    public int size() {                       // return the number of items on the randomized queue
        return nItems;
    }

    public void enqueue(Item item) {          // add the item
        if (item == null)
            throw new IllegalArgumentException("parameter can not be null.");
        items[nItems++] = item;
        // expand array if necessary
        if (nItems >= items.length)
            resize(items.length * 2);
    }

    public Item dequeue() {                   // remove and return a random item
        if (isEmpty())
            throw new NoSuchElementException("empty queue.");
        int randomPos = StdRandom.uniform(nItems);
        Item tmp = items[randomPos];
        items[randomPos] = items[--nItems];
        // shrink array if necessary
        if (nItems <= items.length / 4)
            resize(items.length / 2);
        return tmp;
    }

    private void resize(int length) {
        Item[] newItems = (Item[]) new Object[length];
        for (int i = 0; i < nItems; i++)
            newItems[i] = items[i];
        items = newItems;
    }

    public Item sample() {                    // return a random item (but do not remove it)
        if (isEmpty())
            throw new NoSuchElementException("empty queue.");
        int randomPos = StdRandom.uniform(nItems);
        return items[randomPos];
    }

    public Iterator<Item> iterator() {        // return an independent iterator over items in random order
        return new ListIterator();
    }

    private class ListIterator implements Iterator<Item> {
        private Item[] listItems = (Item[]) new Object[nItems];

        public boolean hasNext() {
            return nItems != 0;
        }

        public Item next() {
            if (isEmpty())
                throw new NoSuchElementException("empty randomized queue.");
            // choose an item randomly
            int randomPos = StdRandom.uniform(nItems);
            // swap the chosen item with the last item in the array and reduce the length
            // of the array by 1.
            Item tmp = listItems[randomPos];
            listItems[randomPos] = listItems[--nItems];
            return tmp;
        }

        public void remove() {
            throw new UnsupportedOperationException("remove not supported.");
        }
    }

    public static void main(String[] args) {  // unit testing (optional)

    }
}