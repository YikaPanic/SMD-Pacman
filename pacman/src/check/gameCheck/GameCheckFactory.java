package src.check.gameCheck;

import src.utility.GameCallback;

public class GameCheckFactory {

    public static GameCheck getGameCheck(int i, GameCallback gc) {
        GameCheck gameCheck = null;
        switch (i) {
            case 1:
                gameCheck = new GameChecking1(gc);
                break;
            case 2:
                gameCheck = new GameChecking2(gc);
                break;
        }
        return gameCheck;
    }
}
