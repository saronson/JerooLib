
/**
 * The snapshot state of a Jeroo 
 * i.e. location, direction and number of flowers it has
 * @author Cameron Christensen
 */
public class JerooState implements Directions {
    private int flowers;
    private int x;
    private int y;
    private CompassDirection direction;
    private String image;


    public JerooState(int y, int x, CompassDirection direction, int flowers, String image){
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.flowers = flowers;
        this.image = image;
    }

    public int getFlowers(){
        return flowers;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public CompassDirection getDirection(){
        return direction;
    }

    public String getImage() {
        return image;
    }

}
