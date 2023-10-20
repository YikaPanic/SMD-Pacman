package src.check.levelCheck;

import src.utility.GameCallback;

public class LevelChecking4 implements LevelCheck {
    GameCallback gamecallback;
    String mapName = "";

    public LevelChecking4(GameCallback gc, String mn) {
        this.gamecallback = gc;
        this.mapName = mn;
    }

    @Override
    public void LogPrint() {
        gamecallback.writeString(" [ " + mapName + " - – less than 2 Gold and Pill" + " ] ");
    }

    @Override
    public int CheckCondition(char[][] tmpMap) {
        int gold = 0;
        int pill = 0;
        for (int y = 0; y < tmpMap.length; y++) {
            for (int x = 0; x < tmpMap[0].length; x++) {
                if (tmpMap[y][x] == 'd') gold++;
                else if (tmpMap[y][x] == 'c') pill++;
            }
        }
        if (gold < 2 || pill < 2) {
            LogPrint();
            return 0;
        }
        return 1;
    }
}
