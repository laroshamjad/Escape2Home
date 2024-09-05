package Visitor;
import planet.cosmiccode.CosmicCode;
import planet.hiddenhaven.SpotTheDifferenceGameView;
import planet.memorymaze.MemoryGame;
import planet.puzzlemania.PuzzleMania;
import views.AdventureGameView;
public interface Visitor {
    public static void visit(CosmicCode p, AdventureGameView adventureGameView) // what occurs when user wants to go to cosmic code
    {

    }

    public static void visit(SpotTheDifferenceGameView p, AdventureGameView adventureGameView) // what occurs when user wants to go to spot the difference
    {

    }

    public static void visit(MemoryGame p, AdventureGameView adventureGameView) // what occurs when user wants to go to memory game
    {

    }

    public static void visit(PuzzleMania p, AdventureGameView adventureGameView) // what occurs when user wants to go to puzzle mania
    {

    }
}
