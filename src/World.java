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
    private ArrayList<Demon> demons = new ArrayList<Demon>();
    private Point topLeftBound;
    private Point botRightBound;
    private int numSinkholes = 0;
    private int numTrees = 0;
    private int numWalls = 0;
    private int numDemons = 0;
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
                        navec = new Navec(xCoord, yCoord);
                        break;
                    case "Demon":
                        demons.add(new PassiveDemon(xCoord, yCoord));
                        numDemons++;
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

    public static Player getFae() {
        return fae;
    }

    public Navec getNavec() {
        return navec;
    }

    /* Draws all the obstacles stored in arrays */
    public void drawObstacles() {
        for (int i = 0; i < numSinkholes; i++) {
            sinkholes.get(i).drawObstacle();
        }
        for (int j = 0; j < numWalls; j++) {
            walls.get(j).drawObstacle();
        }
        for (int k = 0; k < numTrees; k++) {
            trees.get(k).drawObstacle();
        }
    }

    public void drawDemons() {
        for (int i = 0; i < numDemons; i++) {
            demons.get(i).drawCharacter();
        }
        navec.drawCharacter();
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
    private String pointCheck(Obstacle obstacle) {
        Point topLeft = fae.getBoundary().topLeft();
        Point botRight = fae.getBoundary().bottomRight();
        /* checks that either of the two points, 3 pixels in from Fae rectangle border, intersects with a wall and
           returns this edge */
        if (obstacle.getBoundary().intersects(new Point(fae.getBoundary().right(), topLeft.y + WALL_INTERSECT_OFFSET)) ||
                obstacle.getBoundary().intersects(new Point(fae.getBoundary().right(), botRight.y - WALL_INTERSECT_OFFSET)))
            return "right";

        else if (obstacle.getBoundary().intersects(new Point(fae.getBoundary().left(), topLeft.y + WALL_INTERSECT_OFFSET)) ||
                obstacle.getBoundary().intersects(new Point(fae.getBoundary().left(), botRight.y - WALL_INTERSECT_OFFSET)))
            return "left";

        else if (obstacle.getBoundary().intersects(new Point(topLeft.x + WALL_INTERSECT_OFFSET, fae.getBoundary().top())) ||
                obstacle.getBoundary().intersects(new Point(botRight.x - WALL_INTERSECT_OFFSET, fae.getBoundary().top())))
            return "up";

        else if (obstacle.getBoundary().intersects(new Point(topLeft.x + WALL_INTERSECT_OFFSET, fae.getBoundary().bottom())) ||
                obstacle.getBoundary().intersects(new Point(botRight.x - WALL_INTERSECT_OFFSET, fae.getBoundary().bottom())))
            return "down";

        return "";
    }

    /* Checks if Fae is intersecting with any walls and returns true if face of wall hit is same as direction of Fae */
    public boolean obstacleIntersect(String direction) {
        /* iterates through all walls */
        for (Wall wall : walls) {
            /* checks if there's a wall intersection and that the intersected wall face is the same as the direction
               Fae is moving which is the only direction Fae shouldn't be able to move */
            if (wall.getBoundary().intersects(fae.getBoundary()) && pointCheck(wall).equals(direction)) {
                return true;
            }
        }
        for (Tree tree : trees) {
            /* checks if there's a wall intersection and that the intersected wall face is the same as the direction
               Fae is moving which is the only direction Fae shouldn't be able to move */
            if (tree.getBoundary().intersects(fae.getBoundary()) && pointCheck(tree).equals(direction)) {
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
            if (sinkholes.get(i).getBoundary().intersects(fae.getBoundary())) {
                sinkholes.remove(i);
                numSinkholes--;
                return true;
            }
        }
        return false;
    }

    public void demonIntersect() {
        Demon current;
        for (int i = 0; i < numDemons; i++) {
            current = demons.get(i);
            if (current.getBoundary().intersects(fae.getBoundary())) {
                current.setHealth(current.getHealth() - fae.getDamagePoints());
                if (current.isDead()) {
                    demons.remove(i);
                    numDemons--;
                }
            }
        }
        if (navec.getBoundary().intersects(fae.getBoundary())) {
            navec.setHealth(navec.getHealth() - fae.getDamagePoints());
        }
    }


}
