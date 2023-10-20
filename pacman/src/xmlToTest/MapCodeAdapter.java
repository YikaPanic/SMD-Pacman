package src.xmlToTest;

import ch.aplu.jgamegrid.Location;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import src.PacManGameGrid;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MapCodeAdapter implements MapCode {
    private ArrayList<Location> propertyPillLocations = new ArrayList<>();
    private ArrayList<Location> propertyGoldLocations = new ArrayList<>();
    private int[] PacmanXY = {0, 0};
    private int[] XT5XY = {0, 0};
    private int[] TrollXY = {0, 0};
    protected PacManGameGrid grid;

    public MapCodeAdapter(File GameFile) {
        SAXBuilder builder = new SAXBuilder();
        try {
            Document document = (Document) builder.build(GameFile);//jdom is used to interpret the xml file, which has been selected at this point
            Element rootNode = document.getRootElement();
            java.util.List sizeList = rootNode.getChildren("size");
            Element sizeElem = (Element) sizeList.get(0);
            int height = Integer.parseInt(sizeElem
                    .getChildText("height"));
            int width = Integer
                    .parseInt(sizeElem.getChildText("width"));
            java.util.List rows = rootNode.getChildren("row");
            grid = new PacManGameGrid(width, height);
            for (int y = 0; y < rows.size(); y++) {
                Element cellsElem = (Element) rows.get(y);
                List cells = cellsElem.getChildren("cell");

                for (int x = 0; x < cells.size(); x++) {
                    Element cell = (Element) cells.get(x);
                    String cellValue = cell.getText();

                    char tileNr = ' ';
                    if (cellValue.equals("PathTile"))
                        tileNr = ' ';
                    else if (cellValue.equals("WallTile"))
                        tileNr = 'x';
                    else if (cellValue.equals("PillTile")) {
                        tileNr = '.';
                        propertyPillLocations.add(new Location(x, y));
                    } else if (cellValue.equals("GoldTile")) {
                        tileNr = 'g';
                        propertyGoldLocations.add(new Location(x, y));
                    } else if (cellValue.equals("IceTile"))
                        tileNr = 'i';
                    else if (cellValue.equals("PacTile")) {
                        PacmanXY[0] = x;
                        PacmanXY[1] = y;
                    } else if (cellValue.equals("TrollTile")) {
                        TrollXY[0] = x;
                        TrollXY[1] = y;
                    } else if (cellValue.equals("TX5Tile")) {
                        XT5XY[0] = x;
                        XT5XY[1] = y;
                    } else if (cellValue.equals("PortalWhiteTile"))
                        tileNr = '1';
                    else if (cellValue.equals("PortalYellowTile"))
                        tileNr = '2';
                    else if (cellValue.equals("PortalDarkGoldTile"))
                        tileNr = '3';
                    else if (cellValue.equals("PortalDarkGrayTile"))
                        tileNr = '4';

                    grid.setCell(x, y, tileNr);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
