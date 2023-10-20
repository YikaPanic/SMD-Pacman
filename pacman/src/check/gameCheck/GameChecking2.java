package src.check.gameCheck;

import src.utility.GameCallback;

import java.io.File;

public class GameChecking2 implements GameCheck {
    GameCallback gameCallback;

    public GameChecking2(GameCallback gc) {
        gameCallback = gc;
    }

    @Override
    public void LogPrint() {

    }

    @Override
    public boolean CheckCondition(File parentFile) {
        File[] allFile = parentFile.listFiles();//Gets the paths of all files in the parent directory
        int index = 0;
        String[] FileNameList = new String[20];
        int[] FileNumList = new int[20];
        for (int i = 0; i < allFile.length; i++) {
            String FileName = allFile[i].getName();
            if (FileName.charAt(0) < '0' || FileName.charAt(0) > '9') continue;
            FileNumList[index] = 0;
            for (int j = 0; ; j++) {
                if (FileName.charAt(j) < '0' || FileName.charAt(j) > '9') break;
                FileNumList[index] *= 10;
                FileNumList[index] += FileName.charAt(j);
            }
            FileNameList[index] = FileName;
            index++;
        }
        String LogString = "[Game foldername – multiple maps at same level:";
        int[] rep = new int[20];//Duplicate digit
        int repIndex = 0;
        for (int i = 0; i < index; i++) {
            for (int j = i + 1; j < index; j++) {
                if (FileNumList[j] == FileNumList[i]) {
                    rep[repIndex] = FileNumList[i];
                    repIndex++;
                }
            }
        }
        if (repIndex == 0) {
            return true;
        }
        for (int i = 0; i < index; i++) {
            Boolean flag = false;
            for (int j = 0; j < repIndex; j++) {
                if (FileNumList[i] == rep[j]) flag = true;
            }
            if (flag == true) LogString += (FileNameList[i] + " ");
        }
        gameCallback.writeString(LogString);
        return false;
    }
}
