package src.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import src.Game;
import src.check.gameCheck.GameCheck;
import src.check.gameCheck.GameCheckFactory;
import src.check.levelCheck.LevelCheck;
import src.check.levelCheck.LevelCheckFactory;
import src.grid.Camera;
import src.grid.Grid;
import src.grid.GridCamera;
import src.grid.GridModel;
import src.grid.GridView;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import src.utility.GameCallback;
import src.utility.PropertiesLoader;

/**
 * Controller of the application.
 *
 * @author Daniel "MaTachi" Jonsson
 * @version 1
 * @since v0.0.5
 */
public class Controller implements ActionListener, GUIInformation {

    /**
     * The model of the map editor.
     */
    private Grid model;

    private Tile selectedTile;
    private Camera camera;

    private List<Tile> tiles;

    private GridView grid;
    private View view;

    private int gridWith = Constants.MAP_WIDTH;
    private int gridHeight = Constants.MAP_HEIGHT;

    private GameCallback gameCallback;

    private File GameFile;
    private ArrayList<File> GameFileList = new ArrayList<>();

    public String args;

    /**
     * Construct the controller.
     */
    public Controller(GameCallback gc, String args) {
        this.args = args;
        gameCallback = gc;
        File GameFile = new File("Game");
        init(Constants.MAP_WIDTH, Constants.MAP_HEIGHT);
        if (args.equals("") || args == null) {
            ;
        } else if (args.contentEquals(".xml")) {
            File[] allFile = GameFile.listFiles();
            for (int i = 0; i < allFile.length; i++) {
                if (allFile[i].getName().equals(args)) {
                    openTest(allFile[i]);
                    break;
                }
            }
        } else {
            openTest(GameFile);
        }

    }

    public void init(int width, int height) {
        this.tiles = TileManager.getTilesFromFolder("data/");
        this.model = new GridModel(width, height, tiles.get(0).getCharacter());
        this.camera = new GridCamera(model, Constants.GRID_WIDTH,
                Constants.GRID_HEIGHT);

        grid = new GridView(this, camera, tiles); // Every tile is
        // 30x30 pixels

        this.view = new View(this, camera, grid, tiles);
    }

    /**
     * Different commands that comes from the view.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        for (Tile t : tiles) {
            if (e.getActionCommand().equals(
                    Character.toString(t.getCharacter()))) {
                selectedTile = t;
                break;
            }
        }
        if (e.getActionCommand().equals("flipGrid")) {
            // view.flipGrid();
        } else if (e.getActionCommand().equals("save")) {
            if (LevelChekMap()) {
                saveFile();
            } else {
                System.out.println("LevelChecking is not pass");
            }
        } else if (e.getActionCommand().equals("load")) {
            loadFile();
        } else if (e.getActionCommand().equals("update")) {
            updateGrid(gridWith, gridHeight);
        } else if (e.getActionCommand().equals("test")) {
            testGame();
        }
    }

    public void updateGrid(int width, int height) {
        view.close();
        init(width, height);
        view.setSize(width, height);
    }

    DocumentListener updateSizeFields = new DocumentListener() {

        public void changedUpdate(DocumentEvent e) {
            gridWith = view.getWidth();
            gridHeight = view.getHeight();
        }

        public void removeUpdate(DocumentEvent e) {
            gridWith = view.getWidth();
            gridHeight = view.getHeight();
        }

        public void insertUpdate(DocumentEvent e) {
            gridWith = view.getWidth();
            gridHeight = view.getHeight();
        }
    };

    private void saveFile() {

        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "xml files", "xml");
        chooser.setFileFilter(filter);
        File workingDirectory = new File(System.getProperty("user.dir"));
        chooser.setCurrentDirectory(workingDirectory);

        int returnVal = chooser.showSaveDialog(null);
        try {
            if (returnVal == JFileChooser.APPROVE_OPTION) {

                Element level = new Element("level");
                Document doc = new Document(level);
                doc.setRootElement(level);

                Element size = new Element("size");
                int height = model.getHeight();
                int width = model.getWidth();
                size.addContent(new Element("width").setText(width + ""));
                size.addContent(new Element("height").setText(height + ""));
                doc.getRootElement().addContent(size);

                for (int y = 0; y < height; y++) {
                    Element row = new Element("row");
                    for (int x = 0; x < width; x++) {
                        char tileChar = model.getTile(x, y);
                        String type = "PathTile";

                        if (tileChar == 'b')
                            type = "WallTile";
                        else if (tileChar == 'c')
                            type = "PillTile";
                        else if (tileChar == 'd')
                            type = "GoldTile";
                        else if (tileChar == 'e')
                            type = "IceTile";
                        else if (tileChar == 'f')
                            type = "PacTile";
                        else if (tileChar == 'g')
                            type = "TrollTile";
                        else if (tileChar == 'h')
                            type = "TX5Tile";
                        else if (tileChar == 'i')
                            type = "PortalWhiteTile";
                        else if (tileChar == 'j')
                            type = "PortalYellowTile";
                        else if (tileChar == 'k')
                            type = "PortalDarkGoldTile";
                        else if (tileChar == 'l')
                            type = "PortalDarkGrayTile";

                        Element e = new Element("cell");
                        row.addContent(e.setText(type));
                    }
                    doc.getRootElement().addContent(row);
                }
                XMLOutputter xmlOutput = new XMLOutputter();
                xmlOutput.setFormat(Format.getPrettyFormat());
                xmlOutput
                        .output(doc, new FileWriter(chooser.getSelectedFile()));
            }
        } catch (FileNotFoundException e1) {
            JOptionPane.showMessageDialog(null, "Invalid file!", "error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
        }
    }

    public void loadFile() {
        SAXBuilder builder = new SAXBuilder();
        try {
            JFileChooser chooser = new JFileChooser();
            File selectedFile;
            BufferedReader in;
            FileReader reader = null;
            File workingDirectory = new File(System.getProperty("user.dir"));
            chooser.setCurrentDirectory(workingDirectory);

            int returnVal = chooser.showOpenDialog(null);
            Document document;
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                selectedFile = chooser.getSelectedFile();

                if (selectedFile.canRead() && selectedFile.exists()) {
                    document = (Document) builder.build(selectedFile);

                    Element rootNode = document.getRootElement();

                    List sizeList = rootNode.getChildren("size");
                    Element sizeElem = (Element) sizeList.get(0);
                    int height = Integer.parseInt(sizeElem
                            .getChildText("height"));
                    int width = Integer
                            .parseInt(sizeElem.getChildText("width"));
                    updateGrid(width, height);

                    List rows = rootNode.getChildren("row");
                    for (int y = 0; y < rows.size(); y++) {
                        Element cellsElem = (Element) rows.get(y);
                        List cells = cellsElem.getChildren("cell");

                        for (int x = 0; x < cells.size(); x++) {
                            Element cell = (Element) cells.get(x);
                            String cellValue = cell.getText();

                            char tileNr = 'a';
                            if (cellValue.equals("PathTile"))
                                tileNr = 'a';
                            else if (cellValue.equals("WallTile"))
                                tileNr = 'b';
                            else if (cellValue.equals("PillTile"))
                                tileNr = 'c';
                            else if (cellValue.equals("GoldTile"))
                                tileNr = 'd';
                            else if (cellValue.equals("IceTile"))
                                tileNr = 'e';
                            else if (cellValue.equals("PacTile"))
                                tileNr = 'f';
                            else if (cellValue.equals("TrollTile"))
                                tileNr = 'g';
                            else if (cellValue.equals("TX5Tile"))
                                tileNr = 'h';
                            else if (cellValue.equals("PortalWhiteTile"))
                                tileNr = 'i';
                            else if (cellValue.equals("PortalYellowTile"))
                                tileNr = 'j';
                            else if (cellValue.equals("PortalDarkGoldTile"))
                                tileNr = 'k';
                            else if (cellValue.equals("PortalDarkGrayTile"))
                                tileNr = 'l';
                            else
                                tileNr = '0';

                            model.setTile(x, y, tileNr); //map put into the GridModel to store the map content
                        }
                    }

                    String mapString = model.getMapAsString();
                    grid.redrawGrid();

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int FileNameToInt(String name) {
        int num = 0;
        if (name.charAt(0) <= '0' || name.charAt(0) >= '9') return -1;
        for (int i = 0; ; i++) {
            if (name.charAt(i) >= '0' && name.charAt(i) <= '9') {
                num *= 10;
                num += name.charAt(i) - '0';
            } else break;
        }
        return num;
    }

    private void testGame() {
        SAXBuilder builder = new SAXBuilder();
        try {
            JFileChooser chooser = new JFileChooser();
            File selectedFile;
            BufferedReader in;
            FileReader reader = null;
            File workingDirectory = new File(System.getProperty("user.dir"));
            chooser.setCurrentDirectory(workingDirectory);
            chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

            int returnVal = chooser.showOpenDialog(null);
            Document document;
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                selectedFile = chooser.getSelectedFile();
                if (selectedFile.isDirectory()) {
                    if (gameCheckMap(selectedFile)) {
                        File[] allFile = selectedFile.listFiles();
                        int nowFileNum;
                        int min = 200;
                        int minIndex = 0;
                        for (int i = 0; i < allFile.length; i++) {
                            int d = FileNameToInt(allFile[i].getName());
                            if (d < min && d >= 0) {
                                min = d;
                                minIndex = i;
                            }
                        }
                        nowFileNum = FileNameToInt(allFile[minIndex].getName());
                        GameFileList.clear();
                        GameFileList.add(allFile[minIndex]);
                        for (int i = 0; i < allFile.length; i++) {
                            if (FileNameToInt(allFile[i].getName()) == (nowFileNum + 1)) {
                                GameFileList.add(allFile[i]);
                                nowFileNum++;
                                i = -1;
                            }
                        }
                        //System.out.println("length is "+GameFileList.size());
                        GameFile = selectedFile;
                        ThreadGame threadGame = new ThreadGame();
                        threadGame.start();
                        view.close();
                    }
                } else {
                    if (gameCheckMap(selectedFile.getParentFile())) {
                        File fatherFile = selectedFile.getParentFile();
                        File[] allFile = fatherFile.listFiles();
                        int nowFileNum = FileNameToInt(selectedFile.getName());
                        GameFileList.clear();
                        GameFileList.add(selectedFile);
                        for (int i = 0; i < allFile.length; i++) {
                            if (FileNameToInt(allFile[i].getName()) == (nowFileNum + 1)) {
                                GameFileList.add(allFile[i]);
                                nowFileNum++;
                                i = -1;
                            }
                        }
                        //System.out.println("length is "+GameFileList.size());
                        GameFile = selectedFile;
                        ThreadGame threadGame = new ThreadGame();
                        threadGame.start();
                        view.close();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openTest(File selectedFile) {
        if (selectedFile.isDirectory()) {
            if (gameCheckMap(selectedFile)) {
                File[] allFile = selectedFile.listFiles();
                int nowFileNum;
                int min = 200;
                int minIndex = 0;
                for (int i = 0; i < allFile.length; i++) {
                    int d = FileNameToInt(allFile[i].getName());
                    if (d < min && d >= 0) {
                        min = d;
                        minIndex = i;
                    }
                }
                nowFileNum = FileNameToInt(allFile[minIndex].getName());
                GameFileList.clear();
                GameFileList.add(allFile[minIndex]);
                for (int i = 0; i < allFile.length; i++) {
                    if (FileNameToInt(allFile[i].getName()) == (nowFileNum + 1)) {
                        GameFileList.add(allFile[i]);
                        nowFileNum++;
                        i = -1;
                    }
                }
                //System.out.println("length is "+GameFileList.size());
                GameFile = selectedFile;
                ThreadGame threadGame = new ThreadGame();
                threadGame.start();
                view.close();
            }
        } else {
            if (gameCheckMap(selectedFile.getParentFile())) {
                File fatherFile = selectedFile.getParentFile();
                File[] allFile = fatherFile.listFiles();
                int nowFileNum = FileNameToInt(selectedFile.getName());
                GameFileList.clear();
                GameFileList.add(selectedFile);
                for (int i = 0; i < allFile.length; i++) {
                    if (FileNameToInt(allFile[i].getName()) == (nowFileNum + 1)) {
                        GameFileList.add(allFile[i]);
                        nowFileNum++;
                        i = -1;
                    }
                }
                //System.out.println("length is "+GameFileList.size());
                GameFile = selectedFile;
                ThreadGame threadGame = new ThreadGame();
                threadGame.start();
                view.close();
            }
        }
    }

    private boolean LevelChekMap() {
        int checkNum = 0;
        for (int i = 0; ; i++) {
            LevelCheck levelCheck = LevelCheckFactory.getLevelChecking(i + 1, gameCallback);
            if (levelCheck == null) break;
            if (levelCheck.CheckCondition(model.getMap()) == 0) checkNum++;
        }
        if (checkNum != 0) return false;
        return true;
    }

    private boolean gameCheckMap(File parentFile) {
        int checkNum = 0;
        for (int i = 0; ; i++) {
            GameCheck gameCheck = GameCheckFactory.getGameCheck(i + 1, gameCallback);
            if (gameCheck == null) break;
            if (gameCheck.CheckCondition(parentFile) == false) checkNum++;
        }
        if (checkNum != 0) return false;
        return true;
    }

    class ThreadGame extends Thread {
        public void run() {
            for (int i = 0; i < GameFileList.size(); i++) {
                Game game = new Game(gameCallback, GameFileList.get(i));
                if (game.getResult() == false) break;
            }
            updateGrid(gridWith, gridHeight);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Tile getSelectedTile() {
        return selectedTile;
    }
}
