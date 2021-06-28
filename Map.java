/**
 *  @author Cameron Christensen
 *  @author Steve Aronson (modified to allow changing size)
 */
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Map implements Directions {
    // the size of the map grid
    public static final int DEFAULT_WIDTH = 24;
    public static final int DEFAULT_HEIGHT = 24;

    // the data for the map is specified as a matrix of chars
    private char[][] map;

    // for saving the each state of the map from start to finish
    private ArrayList<MapState> history = new ArrayList<MapState>();

    // for keeping track of all of the jeroos on the map
    private ArrayList<JerooBase> jeroos = new ArrayList<>();

    // the singleton...
    private static final Map instance = new Map();

    // for instantiating the singleton

    private Map() {
        map = new char[DEFAULT_HEIGHT][DEFAULT_WIDTH];
        initialize();
    }

    private void initialize() {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                map[i][j] = '.';
            }
        }
        history = new ArrayList<MapState>();
        jeroos = new ArrayList<>();
        saveMap();
    }

    public void changeSize(int rows, int columns) {
        map = new char[rows][columns];
        initialize();
    }

    static public int getRows() {
        return instance.map.length;
    }

    static public int getColumns() {
        return instance.map[0].length;
    } 

    public void addJeroo(JerooBase j) {
        jeroos.add(j);
        saveMap();
    }

    public void removeJeroo(JerooBase j) {
        jeroos.remove(j);
        saveMap();
    }

    public ArrayList<JerooBase> getJeroos() {
        return jeroos;
    }

    public static Map getInstance() {
        return instance;
    }

    public char[][] getCharMatrix(){
        return map;
    }

    public MapState getHistory(int n) {
        return history.get(n);
    }

    public int getHistoryLength() {
        return history.size();
    }

    public Map loadMap(String filename) {
        try {
            Scanner sc = new Scanner(new File(filename));
            String stringMap = "";
            int height = 0;
            int width = -1;

            // Read in each line of the map from the map file
            while (sc.hasNextLine()) {
                String temp = sc.nextLine().trim();
                if (temp.length() > 0) {
                    stringMap += temp;
                    if (width == -1)
                        width = stringMap.length();
                    height++;
                }
            }

            char[][] tempMap = new char[height][width];

            // Transfer data from the String to the char map
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    tempMap[i][j] = stringMap.charAt(i * width + j);
                }
            }
            map = tempMap;

        } catch (Exception e) {
            System.out.println("Failed to load map.");
        }
        return instance;
    }

    public void saveMap(int y, int x, char oldItem, char newItem) {
        MapState mapState = new MapState(jeroos, y, x, oldItem, newItem);
        history.add(mapState);
    }

    public void saveMap() {
        MapState mapState = new MapState(jeroos);
        history.add(mapState);
    }

    public void printMap() {
        char[][] tempMap = new char[map.length][map[0].length];
        for(int i=0; i<map.length; i++){
            for(int j=0; j<map[i].length; j++){
                tempMap[i][j] = map[i][j];
            }
        }
        for(JerooBase j:jeroos){
            tempMap[j.getY()][j.getX()] = getMapChar(j);
        }
        for(int i=0; i<map.length; i++){
            for(int j=0; j<map[i].length; j++){
                System.out.print(tempMap[i][j] + " ");
            }
            System.out.println("");
        }
        System.out.println();
    }

    public void clearSpace(int y, int x) {
        saveMap(y, x, map[y][x], '.');
        map[y][x] = '.';
    }

    public void placeFlower(int y, int x) {
        saveMap(y, x, map[y][x], 'F');
        map[y][x] = 'F';
    }

    public boolean isFlower(int y, int x) {
        return map[y][x] == 'F';
    }

    public boolean isJeroo(int y, int x) {
        for (JerooBase j : jeroos) {
            if (j.getX() == x && j.getY() == y) {
                return true;
            }
        }
        return false;
    }

    public boolean isAnotherJeroo(int y, int x) {
        int count = 0;
        for (JerooBase j : jeroos) {
            if (j.getX() == x && j.getY() == y) {
                count++;
            }
        }
        if (count > 1) {
            return true;
        }
        return true;
    }

    public JerooBase getJerooAt(int y, int x) {
        for (JerooBase j : jeroos) {
            if (j.getX() == x && j.getY() == y) {
                return j;
            }
        }
        return null;
    }

    public boolean isNet(int y, int x) {
        return map[y][x] == 'N';
    }

    public boolean isClear(int y, int x) {

        return !isJeroo(y, x) && map[y][x] == '.';
    }

    public boolean isWater(int y, int x) {
        return map[y][x] == 'W';
    }

    public char getMapChar(JerooBase j) {
        CompassDirection direction = j.getDirection();
        if (direction == NORTH) {
            return '^';
        } else if (direction == EAST) {
            return '>';
        } else if (direction == SOUTH) {
            return 'v';
        } else if (direction == WEST) {
            return '<';
        } else 
            return '?';
    }
}
