package alien;

import java.util.Random;
public class Alien {

    public String name; //name of alien

    public String descrip; //description of alien

    int r1; //first number
    int r2; //second number
    /**
     * Alien constructor
     * __________________________
     * Initializes attributes
     */
    public Alien() {
        this.name = "Mathematician Alien";
        this.descrip = "This is a math alien. He loves math";
        this.r1 =  new Random().nextInt(13) + 1;
        this.r2 =  new Random().nextInt(13) + 1;
    }

    /**
     * getQuestion
     * __________________________
     * returns a random math question
     */
    public String getQuestion() {
        return r1 + " X " + r2 + " =";
    }

    /**
     * getAnswer
     * __________________________
     * returns the right answer for the math question
     */
    public int getAnswer() {
        return r1 * r2;
    }

    /**
     * randomAnswers
     * __________________________
     * generates other random answers to display
     */
    public int randomAnswers() {
        int a = new Random().nextInt(100) + 1;
        if (a == getAnswer()) {
            return a + 1;
        } else {
            return a;
        }
    }

}
