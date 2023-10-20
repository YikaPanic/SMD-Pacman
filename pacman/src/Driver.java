package src;

import src.utility.GameCallback;
import src.utility.PropertiesLoader;
import src.editor.Controller;

import java.util.Properties;

public class Driver {
    public static final String DEFAULT_PROPERTIES_PATH = "properties/test2.properties";

    /**
     * Starting point
     *
     * @param args the command line arguments
     */


    public static void main(String args[]) {
        GameCallback gameCallback = new GameCallback();
        if (args.length != 0) {
            Controller controller = new Controller(gameCallback, args[0]);
        } else {
            Controller controller = new Controller(gameCallback, "");
        }

//        String propertiesPath = DEFAULT_PROPERTIES_PATH;
//        if (args.length > 0) {
//            propertiesPath = args[0];
//        }
//        final Properties properties = PropertiesLoader.loadPropertiesFile(propertiesPath);
//        new Game(gameCallback, properties);
    }
}
