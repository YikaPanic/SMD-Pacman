package src.check.levelCheck;

import src.utility.GameCallback;

public class LevelCheckFactory {
    public static LevelCheck getLevelChecking(int check, GameCallback gcb) {
        LevelCheck lc = null;
        switch (check) {
            case 1:
                lc = new LevelChecking1(gcb, "");
                break;
            case 2:
                lc = new LevelChecking2(gcb, "");
                break;
            case 3:
                lc = new LevelChecking3(gcb, "");
                break;
            case 4:
                lc = new LevelChecking4(gcb, "");
                break;
        }
        return lc;
    }
}
