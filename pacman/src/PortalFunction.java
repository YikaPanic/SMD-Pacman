package src;

import ch.aplu.jgamegrid.Location;

public class PortalFunction {
    private int[][][] Portal = new int[4][2][2];

    public PortalFunction() {
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 2; j++)
                for (int k = 0; k < 2; k++)
                    Portal[i][j][k] = 200;
    }

    //is Portal?
    public Boolean isPortal(int x, int y) {
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 2; j++)
                if (Portal[i][j][0] == x && Portal[i][j][1] == y) return true;
        return false;
    }

    public Location getNetLocation(Location now) {
        Location next = now;
        int x = now.getX();
        int y = now.getY();
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 2; j++)
                if (Portal[i][j][0] == x && Portal[i][j][1] == y) {
                    next = new Location(Portal[i][1 - j][0], Portal[i][1 - j][1]);
                    return next;
                }
        return next;
    }

    public void setWhite(int x, int y) {
        if (Portal[0][0][0] == 200) {
            Portal[0][0][0] = x;
            Portal[0][0][1] = y;
        } else {
            Portal[0][1][0] = x;
            Portal[0][1][1] = y;
        }
    }

    public void setYellow(int x, int y) {
        if (Portal[1][0][0] == 200) {
            Portal[1][0][0] = x;
            Portal[1][0][1] = y;
        } else {
            Portal[1][1][0] = x;
            Portal[1][1][1] = y;
        }
    }

    public void setDGold(int x, int y) {
        if (Portal[2][0][0] == 200) {
            Portal[2][0][0] = x;
            Portal[2][0][1] = y;
        } else {
            Portal[2][1][0] = x;
            Portal[2][1][1] = y;
        }
    }

    public void setDGray(int x, int y) {
        if (Portal[3][0][0] == 200) {
            Portal[3][0][0] = x;
            Portal[3][0][1] = y;
        } else {
            Portal[3][1][0] = x;
            Portal[3][1][1] = y;
        }
    }
}
