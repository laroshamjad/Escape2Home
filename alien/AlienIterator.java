package alien;

import java.util.ArrayList;

public class AlienIterator implements A_Iterator{

    ArrayList<Alien> aliens = new ArrayList<>(); //holds aliens

    int curr; //tracks current alien we are on

    /**
     * AlienIterator constructor
     * __________________________
     * Initializes attributes
     */
    public AlienIterator() {
        Alien one = new Alien();
        Alien two = new Alien();
        Alien three = new Alien();
        this.aliens.add(one);
        this.aliens.add(two);
        this.aliens.add(three);
        this.curr = 0;
    }

    /**
     * hasNext
     * __________________________
     * checks if there are more aliens left
     */
    @Override
    public boolean hasNext() {
        if (this.curr < 3) {
            return true;
        }
        return false;
    }

    /**
     * next
     * __________________________
     * returns the next alien in the array
     */
    @Override
    public Alien next() {
        Alien b = this.aliens.get(this.curr);
        this.curr += 1;
        return b;
    }
    /**
     * remove
     * __________________________
     * removes the alien at beginning of array
     */
    @Override
    public void remove() {
        this.aliens.remove(0);
    }
}
