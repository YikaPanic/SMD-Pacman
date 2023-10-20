package src.check.gameCheck;

import src.utility.GameCallback;

import java.io.File;

public class GameChecking1 implements GameCheck {
    GameCallback gameCallback;

    public GameChecking1(GameCallback gc) {
        gameCallback = gc;
    }

    @Override
    public void LogPrint() {
        gameCallback.writeString(" [Game foldername – no maps found] ");
    }

    @Override
    public boolean CheckCondition(File parentFile) {
        File[] allFile = parentFile.listFiles();//Gets the paths of all files in the parent directory
        for (int i = 0; i < allFile.length; i++) {
            String FileName = allFile[i].getName();
            if (FileName.charAt(0) >= '0' && FileName.charAt(0) <= '9') {
                return true;
            }
        }
        LogPrint();
        return false;
    }
}
