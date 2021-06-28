public interface Directions {

    public enum CompassDirection {
        NORTH, EAST, SOUTH, WEST
    }

    public enum RelativeDirection {
        HERE, AHEAD, LEFT, RIGHT

    }

    public static CompassDirection NORTH = CompassDirection.NORTH;
    public static CompassDirection EAST = CompassDirection.EAST;
    public static CompassDirection SOUTH = CompassDirection.SOUTH;
    public static CompassDirection WEST = CompassDirection.WEST;

    public static RelativeDirection HERE = RelativeDirection.HERE;
    public static RelativeDirection AHEAD = RelativeDirection.AHEAD;
    public static RelativeDirection LEFT = RelativeDirection.LEFT;
    public static RelativeDirection RIGHT = RelativeDirection.RIGHT;

}
