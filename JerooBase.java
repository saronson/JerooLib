/**
 * The JerooBase class defines the basic state and behavior of all Jeroos.
 *
 * @author Cameron Christensen
 * @author Steve Aronson (modified)
 */
public abstract class JerooBase implements Directions{

    private int flowers;
    private CompassDirection direction = EAST;
    private int x = 0;
    private int y = 0;
    private int ops = 0;
    public static boolean showSteps = false;
    private static int imageNum = 0;
    private boolean isCustomImage = false;
    private String northImage = "images/Jeroo_0_North14.gif";
    private String eastImage = "images/Jeroo_0_East14.gif";
    private String southImage ="images/Jeroo_0_South14.gif";
    private String westImage = "images/Jeroo_0_West14.gif";
    private boolean showMovement = true;

    public JerooBase() {
        this(0, 0, EAST, 0);
    }

    public JerooBase(int flowers) {
        this(0, 0, EAST, flowers);
    }

    public JerooBase(int y, int x) {
        this(y, x, EAST, 0);
    }

    public JerooBase(int y, int x, CompassDirection direction) {
        this(y, x, direction, 0);
    }

    public JerooBase(int y, int x, int flowers) {
        this(y, x, EAST, flowers);
    }

    public JerooBase(int y, int x, CompassDirection direction, int flowers) {
        this(y, x, direction, flowers, 
            "images/Jeroo_" + imageNum + "_North14.gif",
            "images/Jeroo_" + imageNum + "_East14.gif",
            "images/Jeroo_" + imageNum + "_South14.gif",
            "images/Jeroo_" + imageNum + "_West14.gif");

        imageNum++;
        imageNum = imageNum % 4;

    }

    public JerooBase(int y, int x, CompassDirection direction, int flowers, String northImage, String eastImage, String southImage, String westImage) 
    {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.flowers = flowers;
        this.northImage = northImage;
        this.eastImage = eastImage;
        this.southImage = southImage;
        this.westImage = westImage;

        // New jeroos must be added to the map.
        Map.getInstance().addJeroo((JerooBase)this);

        if (showSteps) 
            Map.getInstance().printMap();
    }

    // "Action methods" for making a jeroo DO things. Notice these methods have
    // void as a return type and that they don't contain return statements.
    public void hop() {
        ops++;
        int tempX = JerooHelper.findXRelative(AHEAD, direction, x);
        int tempY = JerooHelper.findYRelative(AHEAD, direction, y);
        Map map = Map.getInstance();
        if (JerooHelper.coordsInBounds(tempY, tempX)){
            if (map.isClear(tempY, tempX) || map.isFlower(tempY, tempX)) {
                x = tempX;
                y = tempY;
                if (showMovement) {
                    map.saveMap();
                    if (showSteps) 
                        Map.getInstance().printMap();

                }
            } else {
                if (map.isNet(tempY, tempX)) {
                    throw new Error("Jeroo trapped in net!");
                } else if (map.isWater(tempY, tempX)) {
                    throw new Error("Jeroo drowned in water!");
                } else if (map.isJeroo(tempY, tempX)) {
                    throw new Error("Jeroo has collided with another jeroo!");
                }
            }

        } else {
            throw new Error("Jeroo drowned in water!");
        }
    }

    public void hop(int n) {
        for (int i = 0; i < n; i++) {
            hop();
        }
    }

    public void pick() {
        ops++;
        if (isFlower(HERE)) {
            flowers++;
            Map.getInstance().clearSpace(y, x);
            if (showSteps) 
                Map.getInstance().printMap();

        }
    }

    public void plant() {
        ops++;
        if (flowers > 0) {
            flowers--;
            Map.getInstance().placeFlower(y, x);
            if (showSteps) 
                Map.getInstance().printMap();

        }
    }

    public void toss() {
        ops++;
        if (flowers > 0) {
            flowers--;
            int tempX = JerooHelper.findXRelative(AHEAD, direction, x);
            int tempY = JerooHelper.findYRelative(AHEAD, direction, y);
            if (JerooHelper.coordsInBounds(tempY, tempX)) {
                if (Map.getInstance().isNet(tempY, tempX)) {
                    Map.getInstance().clearSpace(tempY, tempX);
                    Map.getInstance().saveMap();
                } else {
                    Map.getInstance().placeFlower(tempY, tempX);
                }
            }
            if (showSteps) 
                Map.getInstance().printMap();
        }
    }

    public void give(RelativeDirection relDir) {
        ops++;
        if (flowers > 0) {
            int tempX = JerooHelper.findXRelative(relDir, direction, x);
            int tempY = JerooHelper.findYRelative(relDir, direction, y);
            if (JerooHelper.coordsInBounds(tempY, tempX) && Map.getInstance().isJeroo(tempY, tempX)) {
                flowers--;
                Map.getInstance().getJerooAt(tempY, tempX).receiveFlower();
                Map.getInstance().saveMap();
            }
        }
    }

    public void turn(RelativeDirection relDir) {
        ops++;
        if (relDir == RIGHT) {
            if (direction == NORTH) {
                direction = EAST;
            } else if (direction == EAST) {
                direction = SOUTH;
            } else if (direction == SOUTH) {
                direction = WEST;
            } else if (direction == WEST) {
                direction = NORTH;
            }
        } else if (relDir == LEFT) {
            if (direction == NORTH) {
                direction = WEST;
            } else if (direction == EAST) {
                direction = NORTH;
            } else if (direction == SOUTH) {
                direction = EAST;
            } else if (direction == WEST) {
                direction = SOUTH;
            }
        }
        if (showMovement)
            Map.getInstance().saveMap();
        if (showSteps) 
            Map.getInstance().printMap();

    }

    // "Boolean methods" return either of the boolean values true or false.
    public boolean hasFlower() {
        return flowers > 0;
    }

    public boolean isFacing(CompassDirection compDir) {
        return direction.equals(compDir);
    }

    public boolean isFlower(RelativeDirection relDir) {
        int tempX = JerooHelper.findXRelative(relDir, direction, x);
        int tempY = JerooHelper.findYRelative(relDir, direction, y);
        if (JerooHelper.coordsInBounds(tempY, tempX)) {
            return Map.getInstance().isFlower(tempY, tempX);
        }
        return false;
    }

    public boolean isJeroo(RelativeDirection relDir) {
        int tempX = JerooHelper.findXRelative(relDir, direction, x);
        int tempY = JerooHelper.findYRelative(relDir, direction, y);
        if (JerooHelper.coordsInBounds(tempY, tempX)) {
            return Map.getInstance().isJeroo(tempY, tempX);
        }
        return false;
    }

    public boolean isNet(RelativeDirection relDir) {
        int tempX = JerooHelper.findXRelative(relDir, direction, x);
        int tempY = JerooHelper.findYRelative(relDir, direction, y);
        if (JerooHelper.coordsInBounds(tempY, tempX)) {
            return Map.getInstance().isNet(tempY, tempX);
        }
        return false;
    }

    public boolean isWater(RelativeDirection relDir) {
        int tempX = JerooHelper.findXRelative(relDir, direction, x);
        int tempY = JerooHelper.findYRelative(relDir, direction, y);
        if (JerooHelper.coordsInBounds(tempY, tempX)) {
            return Map.getInstance().isWater(tempY, tempX);
        }
        return true;
    }

    public boolean isClear(RelativeDirection relDir) {
        int tempX = JerooHelper.findXRelative(relDir, direction, x);
        int tempY = JerooHelper.findYRelative(relDir, direction, y);
        if (JerooHelper.coordsInBounds(tempY, tempX)) {
            return Map.getInstance().isClear(tempY, tempX);
        }
        return false;
    }

    //  Methods that don't work on Jeroo.org
    /**
     * set to true if you want to see print the map after each operation
     */
    public static void setShowSteps(boolean val) {
        showSteps = val;
    }

    public int getOps() {
        return ops;
    }

    public void resetOps() {
        ops = 0;
    }

    // "Getter" methods for private jeroo instance variables
    // Note: These were NOT part of the original Jeroo API
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public CompassDirection getDirection() {
        return direction;
    }

    public int getFlowers() {
        return flowers;
    }

    // The map needs to be able to increase a jeroo's flowers if another
    // jeroo gave it a flower.
    public void receiveFlower() {
        flowers++;
    }

    /**
     * remove the Jeroo from the map
     */
    public void remove() {
        Map.getInstance().removeJeroo((JerooBase)this);
        if (showSteps) 
            Map.getInstance().printMap();
    }

    /** 
     * randomly return true or false
     */
    public boolean getRandom() {
        int rand = (int)(Math.random()*2);
        return rand == 1;
    }

    public String getImage() {
        if (direction == NORTH) {
            return northImage;
        } else if (direction == EAST) {
            return eastImage;
        } else if (direction == SOUTH) {
            return southImage;
        } else if (direction == WEST) {
            return westImage;
        } else
            return "Bad direction";
    }

    public void setImage(String north, String east, String south, String west) {
        isCustomImage = true;
        northImage = "images/" + north;
        eastImage =  "images/" + east;
        southImage = "images/" + south;
        westImage =  "images/" + west;
    }

    public void showMovement() {
        showMovement = true;
    }

    public void hideMovement() {
        showMovement = false;
    }

    public boolean getShowMovement() {
        return showMovement;
    }

}
