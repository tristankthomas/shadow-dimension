// Class encapsulating the world environment including the player, walls, and sinkholes
import bagel.util.Point;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class World {

    private Player fae;
    private ArrayList<Wall> walls = new ArrayList<Wall>();
    private ArrayList<Sinkhole> sinkholes = new ArrayList<Sinkhole>();
    private Point topLeftBound;
    private Point botRightBound;
    private int numSinkholes = 0;
    private int numWalls = 0;
    private final static int WALL_INTERSECT_OFFSET = 3;

    // initialises all the game entities of the world from a csv file
    public World(String levelFile) {

        int xCoord, yCoord;
        String type;

        try (BufferedReader br = new BufferedReader(new FileReader(levelFile))) {
            String line = null;
            // reads line by line until end of file
            while ((line = br.readLine()) != null) {

                // splits line into elements
                String[] data = line.split(",");

                type = data[0];
                xCoord = Integer.parseInt(data[1]);
                yCoord = Integer.parseInt(data[2]);

                // creates new objects based on data
                switch(type) {
                    case "Player":
                        fae = new Player(xCoord, yCoord);
                        break;
                    case "Wall":
                        walls.add(new Wall(new Point(xCoord, yCoord)));
                        numWalls++;
                        break;
                    case "Sinkhole":
                        sinkholes.add(new Sinkhole(new Point(xCoord, yCoord)));
                        numSinkholes++;
                        break;
                    case "TopLeft":
                        topLeftBound = new Point(xCoord, yCoord);
                    case "BottomRight":
                        botRightBound = new Point(xCoord, yCoord);
                    default:
                        break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Player getFae() {
        return fae;
    }

    // Draws all the sinkholes stored in array
    public void drawSinkholes() {
        for (int i = 0; i < numSinkholes; i++) {
            sinkholes.get(i).drawSinkhole();
        }
    }

    // Draws all the walls stored in array
    public void drawWalls() {
        for (int i = 0; i < numWalls; i++) {
            walls.get(i).drawWall();
        }
    }

    // Checks if player will hit the outside boundary depending on which direction Fae is moving
    public boolean atBoundary(String direction) {
        switch(direction) {
            case "left":
                return fae.getXCoord() <= topLeftBound.x;
            case "right":
                return fae.getXCoord() >= botRightBound.x;
            case "up":
                return fae.getYCoord() <= topLeftBound.y;
            case "down":
                return fae.getYCoord() >= botRightBound.y;
            default:
                return false;
        }

    }


    // Finds the edge of the wall that Fae intersects with
    public String pointCheck(Wall wall) {
        Point topLeft = fae.getRect().topLeft();
        Point botRight = fae.getRect().bottomRight();
        // checks that either of the two points, 3 pixels in from Fae rectangle border, intersects with a wall and
        // returns this edge
        if (wall.getRect().intersects(new Point(fae.getRect().right(), topLeft.y + WALL_INTERSECT_OFFSET)) ||
                wall.getRect().intersects(new Point(fae.getRect().right(), botRight.y - WALL_INTERSECT_OFFSET)))
            return "right";

        else if (wall.getRect().intersects(new Point(fae.getRect().left(), topLeft.y + WALL_INTERSECT_OFFSET)) ||
                wall.getRect().intersects(new Point(fae.getRect().left(), botRight.y - WALL_INTERSECT_OFFSET)))
            return "left";

        else if (wall.getRect().intersects(new Point(topLeft.x + WALL_INTERSECT_OFFSET, fae.getRect().top())) ||
                wall.getRect().intersects(new Point(botRight.x - WALL_INTERSECT_OFFSET, fae.getRect().top())))
            return "up";

        else if (wall.getRect().intersects(new Point(topLeft.x + WALL_INTERSECT_OFFSET, fae.getRect().bottom())) ||
                wall.getRect().intersects(new Point(botRight.x - WALL_INTERSECT_OFFSET, fae.getRect().bottom())))
            return "down";

        return "";
    }

    // move into wall class
    public boolean wallIntersect(String direction) {

        // iterates through all walls
        for (Wall wall : walls) {
            // checks if there's a wall intersection and that the intersected wall face is the same as the direction
            // Fae is moving which is the only direction Fae shouldn't be able to move
            if (wall.getRect().intersects(fae.getRect()) && pointCheck(wall).equals(direction)) {
                return true;
            }
        }
        return false;
    }


    public boolean holeIntersect() {
        // iterates through all sinkhole
        for (int i = 0; i < numSinkholes; i++) {
            // if fae intersects with any sinkhole remove it and decrement number of footpaths
            if (sinkholes.get(i).getRect().intersects(fae.getRect())) {
                sinkholes.remove(i);
                numSinkholes--;
                return true;
            }
        }
        return false;
    }
}
