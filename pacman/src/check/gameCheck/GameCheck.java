package src.check.gameCheck;

import java.io.File;

public interface GameCheck {
    void LogPrint();

    boolean CheckCondition(File parentFile);
}
