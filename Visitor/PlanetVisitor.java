package Visitor;

import alien.AlienSpaceShip;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import planet.cosmiccode.CosmicCode;
import planet.hiddenhaven.SpotTheDifferenceGameView;
import planet.memorymaze.MemoryGame;
import planet.puzzlemania.PuzzleMania;
import views.AdventureGameView;

public class PlanetVisitor implements Visitor {

    /**
     * visit()
     * __________________________
     *
     * Moves player to the next planet, if there are any disturbances it will occur before
     * they go to the planet
     * @param p the next planet the user wants to go to
     * @param adventureGameView the adventure game view
     */
    public static void visit(CosmicCode p, AdventureGameView adventureGameView) {
        if (p.isVisited) {
            adventureGameView.roomDescLabel.setText("You already went to this planet. Choose another.");
            PauseTransition pause2 = new PauseTransition(Duration.seconds(1.0));
            pause2.setOnFinished((event2) -> {
                // after 1 second of displaying that user cannot go again to the same planet, show them their options
                adventureGameView.displayChoice();
            });
            pause2.play();
        } else {
             // call an alien invasion and then play the game
            if (adventureGameView.currentPlanet.equalsIgnoreCase("p")) {
                //call the alien invasion
                AlienSpaceShip spaceship = new AlienSpaceShip(adventureGameView);
                if (spaceship.gameOver()) {
                    adventureGameView.currentPlanet = "c";
                    p.startGame();
                } else {
                    adventureGameView.displayDefeat();
                }
            } else {
                adventureGameView.currentPlanet = "c";
                p.startGame();
            }

        }
    }

    /**
     * visit()
     * __________________________
     *
     * Moves player to the next planet, if there are any disturbances it will occur before
     * they go to the planet
     * @param p the next planet the user wants to go to
     * @param adventureGameView the adventure game view
     */
    public static void visit(SpotTheDifferenceGameView p, AdventureGameView adventureGameView) {
        if (p.isVisited) {
            adventureGameView.roomDescLabel.setText("You already went to this planet. Choose another.");
            PauseTransition pause2 = new PauseTransition(Duration.seconds(1.0));
            pause2.setOnFinished((event2) -> {
                // after 1 second of displaying that user cannot go again to the same planet, show them their options
                adventureGameView.displayChoice();
            });
            pause2.play();
        } else {
            // update the current planet, and play the game
            adventureGameView.currentPlanet = "h";
            p.startGame();
        }

    }

    /**
     * visit()
     * __________________________
     *
     * Moves player to the next planet, if there are any disturbances it will occur before
     * they go to the planet
     * @param p the next planet the user wants to go to
     * @param adventureGameView the adventure game view
     */
    public static void visit(MemoryGame p, AdventureGameView adventureGameView) {
        if (p.isVisited) {
            adventureGameView.roomDescLabel.setText("You already went to this planet. Choose another.");
            PauseTransition pause2 = new PauseTransition(Duration.seconds(1.0));
            pause2.setOnFinished((event2) -> {
                // after 1 second of displaying that user cannot go again to the same planet, show them their options
                adventureGameView.displayChoice();
            });
            pause2.play();
        } else {
            // update the current planet, and play the game
            adventureGameView.currentPlanet = "m";
            p.startGame();
        }

    }

    /**
     * visit()
     * __________________________
     *
     * Moves player to the next planet, if there are any disturbances it will occur before
     * they go to the planet
     * @param p the next planet the user wants to go to
     * @param adventureGameView the adventure game view
     */
    public static void visit(PuzzleMania p, AdventureGameView adventureGameView) {
        if (p.isVisited) {
            adventureGameView.roomDescLabel.setText("You already went to this planet. Choose another.");
            PauseTransition pause2 = new PauseTransition(Duration.seconds(1.0));
            pause2.setOnFinished((event2) -> {
                // after 1 second of displaying that user cannot go again to the same planet, show them their options
                adventureGameView.displayChoice();
            });
            pause2.play();
        } else {
            // call an alien invasion and then play the game
//           if (adventureGameView.currentPlanet.equalsIgnoreCase("c")) {
//                call the alien invasion
//            }
            adventureGameView.currentPlanet = "p";
            p.startGame();
        }
    }
}
