package src.xmlToTest;

import ch.aplu.jgamegrid.Location;
import src.PacManGameGrid;

import java.util.ArrayList;

public interface MapCode {
    int getPacmanX();

    int getPacmanY();

    int getTx5X();

    int getTx5Y();

    int getTrollX();

    int getTrollY();

    PacManGameGrid getGrid();

    ArrayList<Location> getGoldLocation();

    ArrayList<Location> getPillLocation();
}
