/**
 * 
 *  @author Cameron Christensen
 *  @author Steve Aronson (modified)
 */
public class JerooHelper implements Directions {

    public static boolean coordsInBounds(int y, int x) {
        return x >= 0 && x < Map.getColumns() && y >= 0 && y < Map.getRows();
    }

    public static int findXRelative(RelativeDirection relDir, CompassDirection compDir, int x) {
        if (compDir == NORTH) {
            if (relDir == LEFT) {
                x--;
            }
            if (relDir == RIGHT) {
                x++;
            }
        } else if (compDir == EAST) {
            if (relDir == AHEAD) {
                x++;
            }
        } else if (compDir == SOUTH) {
            if (relDir == LEFT) {
                x++;
            }
            if (relDir == RIGHT) {
                x--;
            }
        } else if (compDir == WEST) {
            if (relDir == AHEAD) {
                x--;
            }
        }
        return x;
    }

    public static int findYRelative(RelativeDirection relDir, CompassDirection compDir, int y) {
        if (compDir == EAST) {
            if (relDir == LEFT) {
                y--;
            }
            if (relDir == RIGHT) {
                y++;
            }
        } else if (compDir == SOUTH) {
            if (relDir == AHEAD) {
                y++;
            }
        } else if (compDir == WEST) {
            if (relDir == LEFT) {
                y++;
            }
            if (relDir == RIGHT) {
                y--;
            }
        } else if (compDir == NORTH) {
            if (relDir == AHEAD) {
                y--;
            }
        }
        return y;
    }
}
