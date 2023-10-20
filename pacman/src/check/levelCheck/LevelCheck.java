package src.check.levelCheck;

import src.utility.GameCallback;

public interface LevelCheck {
    void LogPrint();

    int CheckCondition(char[][] tmpMap);
}
