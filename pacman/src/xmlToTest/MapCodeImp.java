package src.xmlToTest;

import ch.aplu.jgamegrid.Location;
import src.PacManGameGrid;

import java.io.File;
import java.util.ArrayList;

public class MapCodeImp implements MapCode {
    private ArrayList<Location> propertyPillLocations = new ArrayList<>();
    private ArrayList<Location> propertyGoldLocations = new ArrayList<>();
    private int[] PacmanXY = {0, 0};
    private int[] XT5XY = {0, 0};
    private int[] TrollXY = {0, 0};
    protected PacManGameGrid grid;
    MapCodeAdapter mapCodeAdapter;

    public MapCodeImp(File gameFile) {
        mapCodeAdapter = new MapCodeAdapter(gameFile);
        PacmanXY[0] = mapCodeAdapter.getPacmanX();
        PacmanXY[1] = mapCodeAdapter.getPacmanY();
        XT5XY[0] = mapCodeAdapter.getTx5X();
        XT5XY[1] = mapCodeAdapter.getTx5Y();
        TrollXY[0] = mapCodeAdapter.getTrollX();
        TrollXY[1] = mapCodeAdapter.getTrollY();
        grid = mapCodeAdapter.getGrid();
        propertyGoldLocations = mapCodeAdapter.getGoldLocation();
        propertyPillLocations = mapCodeAdapter.getPillLocation();
    }

    @Override
    public int getPacmanX() {
        return PacmanXY[0];
    }

    @Override
    public int getPacmanY() {
        return PacmanXY[1];
    }

    @Override
    public int getTx5X() {
        return XT5XY[0];
    }

    @Override
    public int getTx5Y() {
        return XT5XY[1];
    }

    @Override
    public int getTrollX() {
        return TrollXY[0];
    }

    @Override
    public int getTrollY() {
        return TrollXY[1];
    }

    @Override
    public PacManGameGrid getGrid() {
        return grid;
    }

    @Override
    public ArrayList<Location> getGoldLocation() {
        return propertyGoldLocations;
    }

    @Override
    public ArrayList<Location> getPillLocation() {
        return propertyPillLocations;
    }
}
