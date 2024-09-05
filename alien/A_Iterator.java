package alien;

import java.util.Iterator;

public interface A_Iterator extends Iterator<Alien> {

    /**
     * hasNext
     * __________________________
     * checks if there are more aliens left
     */
    public boolean hasNext();

    /**
     * next
     * __________________________
     * returns next alien
     */
    public Alien next();

    /**
     * remove
     * __________________________
     * removes the first alien
     */
    public void remove();

}
