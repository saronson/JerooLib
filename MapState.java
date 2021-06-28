import java.util.ArrayList;
/**
 *  @author Cameron Christensen
 */
public class MapState {

    private ArrayList<JerooState> jerooState = new ArrayList<>();
    private int x = -1;
    private int y = -1;
    private char oldItem = 'o';
    private char newItem = 'n';

    public MapState(ArrayList<JerooBase> jeroos){
        for (JerooBase j : jeroos) {
            
            jerooState.add(new JerooState(j.getY(), j.getX(), j.getDirection(), j.getFlowers(), j.getImage()));
        }
    }
    
    public MapState(ArrayList<JerooBase> jeroos, int y, int x, char oldItem, char newItem) {
        for (JerooBase j : jeroos) {
            jerooState.add(new JerooState(j.getY(), j.getX(), j.getDirection(), j.getFlowers(), j.getImage()));
        }
        this.x = x;
        this.y = y;
        this.oldItem = oldItem;
        this.newItem = newItem;
    }
    
    public int getX(){
        return x;
    }
    
    public int getY(){
        return y;
    }
    
    public char getOldItem(){
        return oldItem;
    }
    
    public char getNewItem(){
        return newItem;
    }
    
    public ArrayList<JerooState> getJerooState(){
        return jerooState;
    }
}
