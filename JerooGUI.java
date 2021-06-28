import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

/**
 *  Jeroo GUI
 *  press right arrow to step forwards and left arrow to step backwards
 *  @author Cameron Christensen
 *  @author Steve Aronson (modified extensively)
 */
public class JerooGUI implements KeyListener {
    public static final Color WATER = Color.CYAN;
    public static final Color GRASS = Color.GREEN;
    public static final Color NET = Color.RED;
    public static final Color FLOWER = Color.YELLOW;
    public static final Color JEROO = new Color(250, 250, 250);
    private int cellSize = 20;
    private int counter;
    private char[][] map;
    private JPanel[][] mapPanels;
    private JFrame frame;

    public JerooGUI() {
        counter = 0;
        map = new char[Map.getRows()][Map.getColumns()];
        mapPanels = new JPanel[Map.getRows()][Map.getColumns()];
        init();
    }

    private void init() {
        frame = new JFrame();
        int tempR = Map.getRows() + 2;
        int tempC = Map.getColumns() + 2;
        frame.setLayout(new GridLayout(tempR, tempC));
        frame.addKeyListener(this);    // JLabel cannot get keyboard focus
        frame.setResizable(true);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int w = (int)screenSize.getWidth();
        int h = (int)screenSize.getHeight();
        int n = w<h?w:h;
        cellSize = n/(tempR<tempC?tempC:tempR);
        frame.setSize(n, n);
        frame.setBackground(WATER);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setTitle("Jeroo");
        frame.requestFocusInWindow();

        // save data to the char maps
        char[][] tempMap = Map.getInstance().getCharMatrix();
        for (int i = 0; i < Map.getRows(); i++) {
            for (int j = 0; j < Map.getColumns(); j++) {
                map[i][j] = tempMap[i][j];
            }
        }

        // save data to the panel map
        for (int i = 0; i < Map.getRows() + 2; i++) {
            for (int j = 0; j < Map.getColumns() + 2; j++) {
                JPanel panel = new JPanel();
                panel.setLayout(new GridBagLayout()); 
                panel.setSize(cellSize, cellSize);
                panel.setVisible(true);
                if (i < 1 || i > Map.getRows() || j < 1 || j > Map.getColumns()) {
                    panel.setBackground(WATER);
                } else {
                    panel.setBackground(GRASS);
                    mapPanels[i - 1][j - 1] = panel;
                }
                frame.add(panel);
            }
        }

        frame.setVisible(true);
        updateMap();
    }

    private void updateMap() {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                char temp = map[i][j];
                Color c = Color.BLACK;
                if (temp == '.') {
                    c = GRASS;
                } else if (temp == 'W') {
                    c = WATER;
                } else if (temp == 'F') {
                    c = FLOWER;
                } else if (temp == 'N') {
                    c = NET;
                }

                mapPanels[i][j].removeAll();
                mapPanels[i][j].setBackground(c);
                mapPanels[i][j].revalidate();
                mapPanels[i][j].repaint();

            }
        }
    }

    public void showArrows(ArrayList<JerooState> jeroos) {
        for (JerooState j : jeroos) {
            mapPanels[j.getY()][j.getX()].removeAll();
            //         mapPanels[j.getY()][j.getX()].setBackground(JEROO);
            String temp = "";
            /*         switch (j.getDirection()) {
            case NORTH  : temp = "\u2191"; break;
            case EAST   : temp = "\u2192"; break;
            case SOUTH  : temp = "\u2193"; break;
            case WEST   : temp = "\u2190"; break;
            }
            temp = "<html><h1>" + temp + "</h1></html>";
             */

            JLabel b = new JLabel(temp, JLabel.CENTER);
            /*
            switch (j.getDirection()) {
                case NORTH  : b.setIcon(new ImageIcon(j.getNorthImage())); break;
                case EAST   : b.setIcon(new ImageIcon(j.getEastImage())); break;
                case SOUTH  : b.setIcon(new ImageIcon(j.getSouthImage())); break;
                case WEST   : b.setIcon(new ImageIcon(j.getWestImage())); break;
            }*/
            String temp2 = j.getImage();
            ImageIcon image = null;
            try {
            java.net.URL url = getClass().getResource(temp2);
            image = new ImageIcon(url);
        } catch (Exception e) {
            image = new ImageIcon(temp2);
        }
            b.setIcon(image);
            b.setVerticalAlignment(JLabel.CENTER);

            // b.setFont(new Font(b.getFont().getName(), Font.BOLD, (int)(b.getFont().getSize())));

            b.setHorizontalAlignment(SwingConstants.CENTER);
            b.setVerticalAlignment(SwingConstants.CENTER);
            mapPanels[j.getY()][j.getX()].add(b);
            mapPanels[j.getY()][j.getX()].revalidate();
            mapPanels[j.getY()][j.getX()].repaint();
        }
    }

    public void stepForwards() {
        if (counter < Map.getInstance().getHistoryLength() - 1) {
            counter++;
            MapState newState = Map.getInstance().getHistory(counter);
            if (newState.getX() >= 0) {
                int x = newState.getX();
                int y = newState.getY();
                char newItem = newState.getNewItem();
                map[y][x] = newItem;
            }
            updateMap();
            ArrayList<JerooState> jeroos = newState.getJerooState();

            showArrows(jeroos);
        }
    }

    public void stepBackwards() {
        if (counter > 0) {
            MapState currentState = Map.getInstance().getHistory(counter);
            counter--;
            if (currentState.getX() >= 0) {
                int x = currentState.getX();
                int y = currentState.getY();
                char oldItem = currentState.getOldItem();
                map[y][x] = oldItem;
            }
            updateMap();
            ArrayList<JerooState> jeroos = Map.getInstance().getHistory(counter).getJerooState();

            showArrows(jeroos);
        }
    }

    public void gotoEnd() {
        if (counter < Map.getInstance().getHistoryLength() - 1) {
            MapState newState = null;
            while (counter < Map.getInstance().getHistoryLength()-1) {
                counter++;
                newState = Map.getInstance().getHistory(counter);
                if (newState.getX() >= 0) {
                    int x = newState.getX();
                    int y = newState.getY();
                    char newItem = newState.getNewItem();
                    map[y][x] = newItem;
                }
            }
            updateMap();
            if (newState != null) {
                ArrayList<JerooState> jeroos = newState.getJerooState();

                showArrows(jeroos);
            }
        }
    }

    public void gotoStart() {
        if (counter > 0) {
            while (counter > 0) {
                MapState currentState = Map.getInstance().getHistory(counter);
                counter--;
                if (currentState.getX() >= 0) {
                    int x = currentState.getX();
                    int y = currentState.getY();
                    char oldItem = currentState.getOldItem();
                    map[y][x] = oldItem;
                }
            }
            updateMap();
            ArrayList<JerooState> jeroos = Map.getInstance().getHistory(counter).getJerooState();

            showArrows(jeroos);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyChar() == 's' || e.getKeyCode() == KeyEvent.VK_LEFT && e.isShiftDown() ) {
            gotoStart();
        } else if (e.getKeyChar() == 'e' || e.getKeyCode() == KeyEvent.VK_RIGHT && e.isShiftDown()) {
            gotoEnd();
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT ) {
            stepBackwards();
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            stepForwards();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}
