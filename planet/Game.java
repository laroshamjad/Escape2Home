package planet;
public interface Game {


    /**
     * geResult
     * _________________
     * returns true if the player has won the game, false otherwise.
     */
    public Boolean getResult(); //return true if player won game, false otherwise.

    /**
     * updateResult
     * _________________
     * updates any changes that might have been made to lives, or other changes that
     * effect the total lives.
     */

    public void updateResult(); //if player lost the game, update their lives.


    /**
     * updateObject
     * _________________
     * displays the tool that the player gains after they win the game.
     */
    public void updateObject(); //if player collected new objects, add them to screen.


    public void startGame(); //start the game from the beginning.
}
