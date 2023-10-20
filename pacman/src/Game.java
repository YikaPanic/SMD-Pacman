// PacMan.java
// Simple PacMan implementation
package src;

import ch.aplu.jgamegrid.*;
import src.monster.Monster;
import src.monster.Troll;
import src.monster.Tx5;
import src.utility.GameCallback;
import src.xmlToTest.MapCodeImp;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Properties;

public class Game extends GameGrid {
    private final static int nbHorzCells = 20;
    private final static int nbVertCells = 11;
    protected PacManGameGrid grid = new PacManGameGrid(nbHorzCells, nbVertCells);

    public PacActor pacActor = new PacActor(this);
    private Monster troll = new Troll(this, MonsterType.Troll);
    private Monster tx5 = new Tx5(this, MonsterType.TX5);


    private ArrayList<Location> pillAndItemLocations = new ArrayList<Location>();
    private ArrayList<Actor> iceCubes = new ArrayList<Actor>();
    private ArrayList<Actor> goldPieces = new ArrayList<Actor>();
    private GameCallback gameCallback;
    private Properties properties;
    private int seed = 30006;
    private ArrayList<Location> propertyPillLocations = new ArrayList<>();
    private ArrayList<Location> propertyGoldLocations = new ArrayList<>();

    private File GameFile;

    private int[] PacmanXY = {0, 0};
    private int[] XT5XY = {0, 0};
    private int[] TrollXY = {0, 0};
    private PortalFunction portalFaction = new PortalFunction();
    private boolean result = false;

    public Game(GameCallback gameCallback, File gf) {
        //Setup game
        super(nbHorzCells, nbVertCells, 20, false);
        this.gameCallback = gameCallback;
        this.properties = properties;
        this.GameFile = gf;
        LoadMapGrid();

        setSimulationPeriod(100);
        setTitle("[PacMan in the Multiverse]");


        GGBackground bg = getBg();
        drawGrid(bg);//Draw maps, draw everything except the two monsters

        //Setup Random seeds
//    seed = Integer.parseInt(properties.getProperty("seed"));
        pacActor.setSeed(seed);
        troll.setSeed(seed);
        tx5.setSeed(seed);
        addKeyRepeatListener(pacActor);
        setKeyRepeatPeriod(150);
        troll.setSlowDown(3);
        tx5.setSlowDown(3);
        pacActor.setSlowDown(3);
        tx5.stopMoving(5);
        setupActorLocations();//There's room for two of them


        //Run the game
        doRun();
        show();
        // Loop to look for collision in the application thread
        // This makes it improbable that we miss a hit
        boolean hasPacmanBeenHit;
        boolean hasPacmanEatAllPills;
        setupPillAndItemsLocations();
        int maxPillsAndItems = countPillsAndItems();

        do {
            hasPacmanBeenHit = troll.getLocation().equals(pacActor.getLocation()) ||
                    tx5.getLocation().equals(pacActor.getLocation());
            hasPacmanEatAllPills = pacActor.getNbPills() >= maxPillsAndItems;
            delay(10);
        } while (!hasPacmanBeenHit && !hasPacmanEatAllPills);
        delay(120);

        Location loc = pacActor.getLocation();
        troll.setStopMoving(true);
        tx5.setStopMoving(true);
        pacActor.removeSelf();

        String title = "";
        if (hasPacmanBeenHit) {
            bg.setPaintColor(Color.red);
            result = false;
            title = "GAME OVER";
            addActor(new Actor("sprites/explosion3.gif"), loc);
        } else if (hasPacmanEatAllPills) {
            bg.setPaintColor(Color.yellow);
            result = true;
            title = "YOU WIN";
        }
        setTitle(title);
        gameCallback.endOfGame(title);
        doPause();
        hide();  //Hide the windows.
    }

    public GameCallback getGameCallback() {
        return gameCallback;
    }

    private void setupActorLocations() {
//    String[] trollLocations = this.properties.getProperty("Troll.location").split(",");
//    String[] tx5Locations = this.properties.getProperty("TX5.location").split(",");
//    String[] pacManLocations = this.properties.getProperty("PacMan.location").split(",");
//    int trollX = Integer.parseInt(trollLocations[0]);
//    int trollY = Integer.parseInt(trollLocations[1]);
//
//    int tx5X = Integer.parseInt(tx5Locations[0]);
//    int tx5Y = Integer.parseInt(tx5Locations[1]);
//
//    int pacManX = Integer.parseInt(pacManLocations[0]);
//    int pacManY = Integer.parseInt(pacManLocations[1]);

        addActor(troll, new Location(TrollXY[0], TrollXY[1]), Location.NORTH);
        addActor(pacActor, new Location(PacmanXY[0], PacmanXY[1]));
        addActor(tx5, new Location(XT5XY[0], XT5XY[1]), Location.NORTH);
    }

    private int countPillsAndItems() {
        int pillsAndItemsCount = 0;
        for (int y = 0; y < nbVertCells; y++) {
            for (int x = 0; x < nbHorzCells; x++) {
                Location location = new Location(x, y);
                int a = grid.getCell(location);
                if (a == 1 && propertyPillLocations.size() == 0) { // Pill
                    pillsAndItemsCount++;
                } else if (a == 3 && propertyGoldLocations.size() == 0) { // Gold
                    pillsAndItemsCount++;
                }
            }
        }
        if (propertyPillLocations.size() != 0) {
            pillsAndItemsCount += propertyPillLocations.size();
        }

        if (propertyGoldLocations.size() != 0) {
            pillsAndItemsCount += propertyGoldLocations.size();
        }

        return pillsAndItemsCount;
    }

    public ArrayList<Location> getPillAndItemLocations() {
        return pillAndItemLocations;
    }


    private void loadPillAndItemsLocations() {
        String pillsLocationString = properties.getProperty("Pills.location");
        if (pillsLocationString != null) {
            String[] singlePillLocationStrings = pillsLocationString.split(";");
            for (String singlePillLocationString : singlePillLocationStrings) {
                String[] locationStrings = singlePillLocationString.split(",");
                propertyPillLocations.add(new Location(Integer.parseInt(locationStrings[0]), Integer.parseInt(locationStrings[1])));
            }
        }

        String goldLocationString = properties.getProperty("Gold.location");
        if (goldLocationString != null) {
            String[] singleGoldLocationStrings = goldLocationString.split(";");
            for (String singleGoldLocationString : singleGoldLocationStrings) {
                String[] locationStrings = singleGoldLocationString.split(",");
                propertyGoldLocations.add(new Location(Integer.parseInt(locationStrings[0]), Integer.parseInt(locationStrings[1])));
            }
        }
    }

    private void setupPillAndItemsLocations() {
        for (int y = 0; y < nbVertCells; y++) {
            for (int x = 0; x < nbHorzCells; x++) {
                Location location = new Location(x, y);
                int a = grid.getCell(location);
                if (a == 1 && propertyPillLocations.size() == 0) {
                    pillAndItemLocations.add(location);
                }
                if (a == 3 && propertyGoldLocations.size() == 0) {
                    pillAndItemLocations.add(location);
                }
                if (a == 4) {
                    pillAndItemLocations.add(location);
                }
            }
        }


        if (propertyPillLocations.size() > 0) {
            for (Location location : propertyPillLocations) {
                pillAndItemLocations.add(location);
            }
        }
        if (propertyGoldLocations.size() > 0) {
            for (Location location : propertyGoldLocations) {
                pillAndItemLocations.add(location);
            }
        }
    }

    private void drawGrid(GGBackground bg) {
        bg.clear(Color.gray);
        bg.setPaintColor(Color.white);
        for (int y = 0; y < nbVertCells; y++) {
            for (int x = 0; x < nbHorzCells; x++) {
                bg.setPaintColor(Color.white);
                Location location = new Location(x, y);
                int a = grid.getCell(location);
                if (a > 0)
                    bg.fillCell(location, Color.lightGray);
                if (a == 1 && propertyPillLocations.size() == 0) { // Pill
                    putPill(bg, location);
                } else if (a == 3 && propertyGoldLocations.size() == 0) { // Gold
                    putGold(bg, location);
                } else if (a == 4) {
                    putIce(bg, location);
                } else if (a == 5) {
                    Actor pw = new Actor("data/i_portalWhiteTile.png");
                    portalFaction.setWhite(x, y);
                    addActor(pw, location);
                } else if (a == 6) {
                    Actor py = new Actor("data/j_portalYellowTile.png");
                    portalFaction.setYellow(x, y);
                    addActor(py, location);
                } else if (a == 7) {
                    Actor pgold = new Actor("data/k_portalDarkGoldTile.png");
                    portalFaction.setDGold(x, y);
                    addActor(pgold, location);
                } else if (a == 8) {
                    Actor pgray = new Actor("data/l_portalDarkGrayTile.png");
                    portalFaction.setDGray(x, y);
                    addActor(pgray, location);
                }
            }
        }

        for (Location location : propertyPillLocations) {
            putPill(bg, location);
        }

        for (Location location : propertyGoldLocations) {
            putGold(bg, location);
        }
    }

    private void putPill(GGBackground bg, Location location) {
        bg.fillCircle(toPoint(location), 5);
    }

    private void putGold(GGBackground bg, Location location) {
        bg.setPaintColor(Color.yellow);
        bg.fillCircle(toPoint(location), 5);
        Actor gold = new Actor("sprites/gold.png");
        this.goldPieces.add(gold);
        addActor(gold, location);
    }

    private void putIce(GGBackground bg, Location location) {
        bg.setPaintColor(Color.blue);
        bg.fillCircle(toPoint(location), 5);
        Actor ice = new Actor("sprites/ice.png");
        this.iceCubes.add(ice);
        addActor(ice, location);
    }

    public void removeItem(String type, Location location) {
        if (type.equals("gold")) {
            for (Actor item : this.goldPieces) {
                if (location.getX() == item.getLocation().getX() && location.getY() == item.getLocation().getY()) {
                    item.hide();
                }
            }
        } else if (type.equals("ice")) {
            for (Actor item : this.iceCubes) {
                if (location.getX() == item.getLocation().getX() && location.getY() == item.getLocation().getY()) {
                    item.hide();
                }
            }
        }
    }

    public Location isPortal(Location now) {
        if (!portalFaction.isPortal(now.getX(), now.getY())) return now;
        return portalFaction.getNetLocation(now);
    }

    private void LoadMapGrid() {

        MapCodeImp mapCodeImp = new MapCodeImp(GameFile);
        PacmanXY[0] = mapCodeImp.getPacmanX();
        PacmanXY[1] = mapCodeImp.getPacmanY();
        XT5XY[0] = mapCodeImp.getTx5X();
        XT5XY[1] = mapCodeImp.getTx5Y();
        TrollXY[0] = mapCodeImp.getTrollX();
        TrollXY[1] = mapCodeImp.getTrollY();
        grid = mapCodeImp.getGrid();
        propertyGoldLocations = mapCodeImp.getGoldLocation();
        propertyPillLocations = mapCodeImp.getPillLocation();
    }

    public int getNumHorzCells() {
        return this.nbHorzCells;
    }

    public int getNumVertCells() {
        return this.nbVertCells;
    }

    public boolean getResult() {
        return result;
    }
}
