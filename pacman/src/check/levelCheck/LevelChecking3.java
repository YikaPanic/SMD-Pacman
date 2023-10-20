package src.check.levelCheck;

import src.utility.GameCallback;

public class LevelChecking3 implements LevelCheck {
    GameCallback gamecallback;
    String mapName = "";

    public LevelChecking3(GameCallback gc, String mn) {
        this.gamecallback = gc;
        this.mapName = mn;
    }

    @Override
    public void LogPrint() {

    }

    @Override
    public int CheckCondition(char[][] tmpMap) {
        int whiteNum = 0;
        String whiteLoc = "";
        int yellowNum = 0;
        String yellowLoc = "";
        int goldNum = 0;
        String goldLoc = "";
        int grayNum = 0;
        String grayLoc = "";
        for (int y = 0; y < tmpMap.length; y++) {
            for (int x = 0; x < tmpMap[0].length; x++) {
                switch (tmpMap[y][x]) {
                    case 'i':
                        whiteNum++;
                        whiteLoc += "(" + x + "," + y + ");";
                        break;
                    case 'j':
                        yellowNum++;
                        yellowLoc += "(" + x + "," + y + ");";
                        break;
                    case 'k':
                        goldNum++;
                        goldLoc += "(" + x + "," + y + ");";
                        break;
                    case 'l':
                        grayNum++;
                        grayLoc += "(" + x + "," + y + ");";
                        break;
                }
            }
        }
        if (whiteNum != 2 || yellowNum != 2 || goldNum != 2 || grayNum != 2) {
            if (whiteNum != 2) {
                gamecallback.writeString(
                        " [ " + mapName + " – portal White count is not 2: " + whiteLoc + " ] ");
            }
            if (yellowNum != 2) {
                gamecallback.writeString(
                        " [ " + mapName + " – portal yellow count is not 2: " + yellowLoc + " ] ");
            }
            if (goldNum != 2) {
                gamecallback.writeString(
                        " [ " + mapName + " – portal dark gold count is not 2: " + goldLoc + " ] ");
            }
            if (grayNum != 2) {
                gamecallback.writeString(
                        " [ " + mapName + " – portal dark gray count is not 2: " + grayLoc + " ] ");
            }
            return 0;
        }
        return 1;
    }
}
