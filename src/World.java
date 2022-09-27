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
import java.util.Random;
import bagel.Image;

public class World {

    private static Player fae = new Player();
    private Navec navec;
    /* array list used here to simplify the removing process and increase memory efficiency */
    private ArrayList<Sinkhole> sinkholes = new ArrayList<Sinkhole>();
    private ArrayList<Tree> trees = new ArrayList<Tree>();
    private ArrayList<Wall> walls = new ArrayList<Wall>();
    private ArrayList<Demon> demons = new ArrayList<Demon>();
    private Random random = new Random();
    private Point topLeftBound;
    private Point botRightBound;
    private int numSinkholes = 0;
    private int numTrees = 0;
    private int numWalls = 0;
    private int numDemons = 0;
    private final static int WALL_INTERSECT_OFFSET = 3;

    /* Initialises all the game entities of the world from a csv file */
    public World(String levelFile) {

        double xCoord, yCoord;
        String type;

        try (BufferedReader br = new BufferedReader(new FileReader(levelFile))) {
            String line;
            /* reads line by line until end of file */
            while ((line = br.readLine()) != null) {

                /* splits line into elements */
                String[] data = line.split(",");

                type = data[0];
                xCoord = Double.parseDouble(data[1]);
                yCoord = Double.parseDouble(data[2]);

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
                        if (random.nextBoolean()) {
                            demons.add(new PassiveDemon(xCoord, yCoord));
                        } else {
                            demons.add(new AggressiveDemon(xCoord, yCoord));
                        }

                        numDemons++;
                        break;
                    case "TopLeft":
                        topLeftBound = new Point(xCoord, yCoord);
                        break;
                    case "BottomRight":
                        xCoord = xCoord + fae.getCurrentImage().getWidth();
                        yCoord = yCoord + fae.getCurrentImage().getHeight();
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
    public boolean atBoundary(String direction, Character character) {
        switch(direction) {
            case "left":
                return character.getXCoord() <= topLeftBound.x;
            case "right":
                return character.getXCoord() + character.getCurrentImage().getWidth() >= botRightBound.x;
            case "up":
                return character.getYCoord() <= topLeftBound.y;
            case "down":
                return character.getYCoord() + character.getCurrentImage().getHeight() >= botRightBound.y;
            default:
                return false;
        }

    }


    /* Finds the edge of the wall that Fae intersects with */
    private String pointCheck(Obstacle obstacle, Character character) {
        Point topLeft = character.getBoundary().topLeft();
        Point botRight = character.getBoundary().bottomRight();
        /* checks that either of the two points, 3 pixels in from Fae rectangle border, intersects with a wall and
           returns this edge */
        if (obstacle.getBoundary().intersects(new Point(character.getBoundary().right(), topLeft.y + WALL_INTERSECT_OFFSET)) ||
                obstacle.getBoundary().intersects(new Point(character.getBoundary().right(), botRight.y - WALL_INTERSECT_OFFSET)))
            return "right";

        else if (obstacle.getBoundary().intersects(new Point(character.getBoundary().left(), topLeft.y + WALL_INTERSECT_OFFSET)) ||
                obstacle.getBoundary().intersects(new Point(character.getBoundary().left(), botRight.y - WALL_INTERSECT_OFFSET)))
            return "left";

        else if (obstacle.getBoundary().intersects(new Point(topLeft.x + WALL_INTERSECT_OFFSET, character.getBoundary().top())) ||
                obstacle.getBoundary().intersects(new Point(botRight.x - WALL_INTERSECT_OFFSET, character.getBoundary().top())))
            return "up";

        else if (obstacle.getBoundary().intersects(new Point(topLeft.x + WALL_INTERSECT_OFFSET, character.getBoundary().bottom())) ||
                obstacle.getBoundary().intersects(new Point(botRight.x - WALL_INTERSECT_OFFSET, character.getBoundary().bottom())))
            return "down";

        return "";
    }

    /* Checks if Fae is intersecting with any walls and returns true if face of wall hit is same as direction of Fae */
    public boolean obstacleIntersect(String direction, Character character) {
        /* iterates through all walls */
        for (Wall wall : walls) {
            /* checks if there's a wall intersection and that the intersected wall face is the same as the direction
               Fae is moving which is the only direction Fae shouldn't be able to move */
            if (wall.getBoundary().intersects(character.getBoundary()) && pointCheck(wall, character).equals(direction)) {
                return true;
            }
        }
        for (Tree tree : trees) {
            /* checks if there's a wall intersection and that the intersected wall face is the same as the direction
               Fae is moving which is the only direction Fae shouldn't be able to move */
            if (tree.getBoundary().intersects(character.getBoundary()) && pointCheck(tree, character).equals(direction)) {
                return true;
            }
        }

        return false;
    }


    /* Checks if Fae intersects with any of the sinkholes and removes it if so */
    public boolean holeIntersect(Character character) {
        /* iterates through all sinkhole */
        for (int i = 0; i < numSinkholes; i++) {
            /* if fae intersects with any sinkhole remove it and decrement number of footpaths */
            if (sinkholes.get(i).getBoundary().intersects(character.getBoundary())) {
                if (character instanceof Player) {
                    sinkholes.remove(i);
                    numSinkholes--;
                }

                return true;
            }
        }
        return false;
    }

    public void faeDemonIntersect() {
        Demon current;
        for (int i = 0; i < numDemons; i++) {
            current = demons.get(i);
            if (current.getBoundary().intersects(fae.getBoundary()) && current.state != State.INVISIBLE) {
                inflictDamage(fae, current, "Fae", "Demon");
                if (current.isDead()) {
                    demons.remove(i);
                    numDemons--;
                }
                current.invincible();
            }
        }
        if (navec.getBoundary().intersects(fae.getBoundary()) && navec.state != State.INVISIBLE) {
            inflictDamage(fae, navec, "Fae", "Navec");
            navec.invincible();
        }

    }

    public void demonInvincible() {
        for (Demon demon : demons) {
            if (demon.state == State.INVISIBLE) {
                demon.invincible();

            }
        }
        if (navec.state == State.INVISIBLE) navec.invincible();
    }

    public boolean demonIntersect(Demon demon) {
        return obstacleIntersect(demon.direction, demon) || holeIntersect(demon) || atBoundary(demon.direction, demon);
    }

    public void demonsMove() {
        for (Demon demon : demons) {
            if (demon.getCanMove()) {
                demon.move(this);
            }
        }
        navec.move(this);
    }

    public Location fireLocation(Demon demon) {
        Point faeCentre = fae.getCentredCoord();
        Point demonCentre = demon.getCentredCoord();
        if (faeCentre.x <= demonCentre.x && faeCentre.y <= demonCentre.y) return Location.TOP_LEFT;
        else if (faeCentre.x <= demonCentre.x && faeCentre.y > demonCentre.y) return Location.BOTTOM_LEFT;
        else if (faeCentre.x > demonCentre.x && faeCentre.y <= demonCentre.y) return Location.TOP_RIGHT;
        else if (faeCentre.x > demonCentre.x && faeCentre.y > demonCentre.y) return Location.BOTTOM_RIGHT;
        return null;
    }

    public void proximityCheck() {
        Point faeCentre = fae.getCentredCoord();
        Point demonCentre;

        for (Demon demon : demons) {
            demonCentre = demon.getCentredCoord();
            if (faeCentre.distanceTo(demonCentre) <= demon.getAttackRange()) {
                demon.attack(this);
            }

        }
        demonCentre = navec.getCentredCoord();
        if (faeCentre.distanceTo(demonCentre) <= navec.getAttackRange()) {
            navec.attack(this);
        }

    }

    public void inflictDamage(Character inflictor, Character inflictee, String inflictorString, String inflicteeString) {
        inflictee.setHealth(inflictee.getHealth() - inflictor.getDamagePoints());
        System.out.printf("%s inflicts %d damage points on %s. %s's current health: %d/%d\n",
                inflictorString, inflictor.getDamagePoints(), inflicteeString, inflicteeString, inflictee.getHealth(), inflictee.getMaxHealth());
    }


}
