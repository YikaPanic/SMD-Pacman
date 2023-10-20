package src.check.levelCheck;

import src.utility.GameCallback;

public class LevelChecking2 implements LevelCheck {
    GameCallback gamecallback;
    String mapName = "";
    String location = "";

    public LevelChecking2(GameCallback gc, String mn) {
        this.gamecallback = gc;
        this.mapName = mn;
    }

    @Override
    public void LogPrint() {
        gamecallback.writeString(" [ " + mapName + " - more than one start for Pacman: " + location + " ] ");
    }

    @Override
    public int CheckCondition(char[][] tmpMap) {
        int pacManNum = 0;
        for (int y = 0; y < tmpMap.length; y++) {
            for (int x = 0; x < tmpMap[0].length; x++) {
                if (tmpMap[y][x] == 'f') {
                    pacManNum++;
                    location += "(" + x + "," + y + ");";
                }
            }
        }
        if (pacManNum > 1) {
            LogPrint();
            return 0;
        }
        return 1;
    }
}
