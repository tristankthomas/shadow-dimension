/**
 * SWEN20003 Project 2, Semester 2, 2022
 *
 * World.java: Class encapsulating the world environment including the player, demons and obstacles
 *
 * @author Tristan Thomas
 */

import bagel.util.Point;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class World {

    private static Player fae = new Player();
    private Navec navec;
    /* array list used here to simplify the removing process and increase memory efficiency */
    private ArrayList<Sinkhole> sinkholes = new ArrayList<Sinkhole>();
    private ArrayList<Tree> trees = new ArrayList<Tree>();
    private ArrayList<Wall> walls = new ArrayList<Wall>();
    private ArrayList<PassiveDemon> passiveDemons = new ArrayList<PassiveDemon>();
    private ArrayList<AggressiveDemon> aggressiveDemons = new ArrayList<AggressiveDemon>();
    private Point topLeftBound;
    private Point botRightBound;
    private int numSinkholes = 0;
    private int numTrees = 0;
    private int numWalls = 0;
    private final static int WALL_INTERSECT_OFFSET = 3;

    /* Initialises all the game entities of the world from a csv file */
    public World(String levelFile) {

        int xCoord, yCoord;
        String type;

        try (BufferedReader br = new BufferedReader(new FileReader(levelFile))) {
            String line;
            /* reads line by line until end of file */
            while ((line = br.readLine()) != null) {

                /* splits line into elements */
                String[] data = line.split(",");

                type = data[0];
                xCoord = Integer.parseInt(data[1]);
                yCoord = Integer.parseInt(data[2]);

                /* creates new objects based on data */
                switch(type) {
                    case "Fae":
                        fae.setXCoord(xCoord);
                        fae.setYCoord(yCoord);
                        break;
                    case "Wall":
                        walls.add(new Wall(new Point(xCoord, yCoord)));
                        numWalls++;
                        break;
                    case "Sinkhole":
                        sinkholes.add(new Sinkhole(new Point(xCoord, yCoord)));
                        numSinkholes++;
                        break;
                    case "Tree":
                        trees.add(new Tree(new Point(xCoord, yCoord)));
                        numTrees++;
                        break;
                    case "Navec":
                        navec = new Navec();
                        break;
                    case "Demon":
                        break;
                    case "TopLeft":
                        topLeftBound = new Point(xCoord, yCoord);
                        break;
                    case "BottomRight":
                        botRightBound = new Point(xCoord, yCoord);
                        break;
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

    /* Draws all the obstacles stored in arrays */
    public void drawObstacles() {
        for (int i = 0; i < numSinkholes; i++) {
            sinkholes.get(i).drawObstacle();
        }
        for (int i = 0; i < numWalls; i++) {
            walls.get(i).drawObstacle();
        }
        for (int i = 0; i < numTrees; i++) {
            trees.get(i).drawObstacle();
        }
    }

    /* Checks if player will hit the outside boundary depending on which direction Fae is moving */
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


    /* Finds the edge of the wall that Fae intersects with */
    private String pointCheck(Wall wall) {
        Point topLeft = fae.getRect().topLeft();
        Point botRight = fae.getRect().bottomRight();
        /* checks that either of the two points, 3 pixels in from Fae rectangle border, intersects with a wall and
           returns this edge */
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

    /* Checks if Fae is intersecting with any walls and returns true if face of wall hit is same as direction of Fae */
    public boolean wallIntersect(String direction) {

        /* iterates through all walls */
        for (Wall wall : walls) {
            /* checks if there's a wall intersection and that the intersected wall face is the same as the direction
               Fae is moving which is the only direction Fae shouldn't be able to move */
            if (wall.getRect().intersects(fae.getRect()) && pointCheck(wall).equals(direction)) {
                return true;
            }
        }
        return false;
    }

    /* Checks if Fae intersects with any of the sinkholes and removes it if so */
    public boolean holeIntersect() {
        /* iterates through all sinkhole */
        for (int i = 0; i < numSinkholes; i++) {
            /* if fae intersects with any sinkhole remove it and decrement number of footpaths */
            if (sinkholes.get(i).getRect().intersects(fae.getRect())) {
                sinkholes.remove(i);
                numSinkholes--;
                return true;
            }
        }
        return false;
    }
}
