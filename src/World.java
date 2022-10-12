import bagel.util.Point;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;

/**
 * SWEN20003 Project 2, Semester 2, 2022
 *
 * World.java: Class encapsulating the world environment including the player, demons and obstacles
 *
 * @author Tristan Thomas
 */
public class World {

    private Player fae;
    private Navec navec;
    /* array list used here to simplify the removing process and increase memory efficiency */
    private ArrayList<Sinkhole> sinkholes = new ArrayList<>();
    private ArrayList<Tree> trees = new ArrayList<>();
    private ArrayList<Wall> walls = new ArrayList<>();
    private ArrayList<Demon> demons = new ArrayList<>();
    private Random random = new Random();
    private static final int TIMESCALE_LOWER = -3;
    private static final int TIMESCALE_UPPER = 3;

    private static final int DEFAULT_TIMESCALE = 0;
    private static int timescale = DEFAULT_TIMESCALE;
    private Point topLeftBound;
    private Point botRightBound;
    private int numSinkholes = 0;
    private int numTrees = 0;
    private int numWalls = 0;
    private int numDemons = 0;
    private final static int WALL_INTERSECT_OFFSET = 3;
    private final static double TIMESCALE_FACTOR = 0.5;
    private final static String DAMAGE_LOG_MESSAGE = "%s inflicts %d damage points on %s. %s's current health: %d/%d\n";
    private final static String SPEED_UP_MESSAGE = "Sped up, Speed: ";
    private final static String SLOW_DOWN_MESSAGE = "Slowed down, Speed: ";

    /**
     * Initialises all the game entities of the world from a csv file
     * @param levelFile
     */
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

    /* Getters */
    public Player getFae() {
        return fae;
    }

    public Navec getNavec() {
        return navec;
    }

    /**
     * Draws all the obstacles stored in arrays
     */
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

    /**
     * Draws all the demons
     */
    public void drawDemons() {
        for (int i = 0; i < numDemons; i++) {
            demons.get(i).drawCharacter();
        }
        navec.drawCharacter();
    }


    /**
     * Checks if player will hit the outside boundary depending on which direction Fae is moving
     * @param direction
     * @param character
     * @return
     */
    public boolean atBoundary(Direction direction, Character character) {
        switch(direction) {
            case LEFT:
                return character.getXCoord() <= topLeftBound.x;
            case RIGHT:
                return character.getXCoord() + character.getCurrentImage().getWidth() >= botRightBound.x;
            case UP:
                return character.getYCoord() <= topLeftBound.y;
            case DOWN:
                return character.getYCoord() + character.getCurrentImage().getHeight() >= botRightBound.y;
            default:
                return false;
        }

    }


    /**
     * Finds the edge of the wall that Fae intersects with
     * @param obstacle
     * @param character
     * @return
     */
    private Direction pointCheck(Obstacle obstacle, Character character) {
        Point topLeft = character.getBoundary().topLeft();
        Point botRight = character.getBoundary().bottomRight();
        /* checks that either of the two points, 3 pixels in from Fae rectangle border, intersects with a wall and
           returns this edge */
        if (obstacle.getBoundary().intersects(new Point(character.getBoundary().right(), topLeft.y + WALL_INTERSECT_OFFSET)) ||
                obstacle.getBoundary().intersects(new Point(character.getBoundary().right(), botRight.y - WALL_INTERSECT_OFFSET)))
            return Direction.RIGHT;

        else if (obstacle.getBoundary().intersects(new Point(character.getBoundary().left(), topLeft.y + WALL_INTERSECT_OFFSET)) ||
                obstacle.getBoundary().intersects(new Point(character.getBoundary().left(), botRight.y - WALL_INTERSECT_OFFSET)))
            return Direction.LEFT;

        else if (obstacle.getBoundary().intersects(new Point(topLeft.x + WALL_INTERSECT_OFFSET, character.getBoundary().top())) ||
                obstacle.getBoundary().intersects(new Point(botRight.x - WALL_INTERSECT_OFFSET, character.getBoundary().top())))
            return Direction.UP;

        else if (obstacle.getBoundary().intersects(new Point(topLeft.x + WALL_INTERSECT_OFFSET, character.getBoundary().bottom())) ||
                obstacle.getBoundary().intersects(new Point(botRight.x - WALL_INTERSECT_OFFSET, character.getBoundary().bottom())))
            return Direction.DOWN;

        return Direction.NULL;
    }


    /**
     * Checks if Fae (or demon) is intersecting with any walls or trees and returns true if face of wall hit is same as direction of Fae
     * @param direction
     * @param character
     * @return
     */
    public boolean obstacleIntersect(Direction direction, Character character) {
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


    /**
     * Checks if Fae or demon intersects with any of the sinkholes and removes it if fae intersects
     * @param character
     * @return
     */
    public boolean holeIntersect(Character character) {
        /* iterates through all sinkhole */
        for (int i = 0; i < numSinkholes; i++) {
            /* if fae intersects with any sinkhole remove it and decrement number of footpaths */
            if (sinkholes.get(i).getBoundary().intersects(character.getBoundary())) {
                /* only remove and inflict damage if character is fae */
                if (character instanceof Player) {
                    inflictDamage(sinkholes.get(i), character, "Sinkhole", "Fae");
                    sinkholes.remove(i);
                    numSinkholes--;
                }

                return true;
            }
        }
        return false;
    }


    /**
     * Checks if fae intersects with a demon and inflicts damage if so
     */
    public void faeDemonIntersect() {
        Demon current;
        for (int i = 0; i < numDemons; i++) {
            current = demons.get(i);
            /* only can attack demon if invincible */
            if (current.getBoundary().intersects(fae.getBoundary()) && !current.isInvincible) {
                inflictDamage(fae, current, "Fae", "Demon");
                if (current.isDead()) {
                    demons.remove(i);
                    numDemons--;
                }
                /* enters invincible state */
                current.invincible();
            }
        }
        if (navec.getBoundary().intersects(fae.getBoundary()) && !navec.isInvincible) {
            inflictDamage(fae, navec, "Fae", "Navec");
            navec.invincible();
        }

    }

    /**
     * Updates invincibility for all characters
     */
    public void charactersInvincible() {
        for (Demon demon : demons) {
            if (demon.isInvincible) {
                demon.invincible();

            }
        }
        if (navec.isInvincible) navec.invincible();
        if (fae.isInvincible) fae.invincible();
    }


    /**
     * Checks if a demon needs to change direction
     * @param demon
     * @return
     */
    public boolean demonIntersect(Demon demon) {
        return obstacleIntersect(demon.direction, demon) || holeIntersect(demon) || atBoundary(demon.direction, demon);
    }

    /**
     * Updates all the demons position and direction
     */
    public void demonsMove() {
        for (Demon demon : demons) {
            if (demon.getCanMove()) {
                demon.move(this);
            }
        }
        navec.move(this);
    }

    /**
     * Calculates location that fire should be shot
     * @param demon
     * @return
     */
    public Location fireLocation(Demon demon) {
        Point faeCentre = fae.getCentredCoord();
        Point demonCentre = demon.getCentredCoord();

        /* calculates location based on relative centre coordinates */
        if (faeCentre.x <= demonCentre.x && faeCentre.y <= demonCentre.y) return Location.TOP_LEFT;
        else if (faeCentre.x <= demonCentre.x && faeCentre.y > demonCentre.y) return Location.BOTTOM_LEFT;
        else if (faeCentre.x > demonCentre.x && faeCentre.y <= demonCentre.y) return Location.TOP_RIGHT;
        else if (faeCentre.x > demonCentre.x && faeCentre.y > demonCentre.y) return Location.BOTTOM_RIGHT;

        return null;
    }

    /**
     * Checks if demons should shoot fire (based on attack range)
     */
    public void demonProximityCheck() {
        Point faeCentre = fae.getCentredCoord();
        Point demonCentre;

        for (Demon demon : demons) {
            demonCentre = demon.getCentredCoord();
            /* demons should attack if so */
            if (faeCentre.distanceTo(demonCentre) <= demon.getAttackRange()) {
                demon.attack(this);
            }

        }
        demonCentre = navec.getCentredCoord();
        if (faeCentre.distanceTo(demonCentre) <= navec.getAttackRange()) {
            navec.attack(this);
        }

    }

    /**
     * Checks if fae is intersecting with a flame
     * @param demon
     */
    public void flameIntersect(Demon demon) {

        if (demon.fire.getBoundary().intersects(fae.getBoundary())) {
            if (!fae.isInvincible) {
                /* inflicts damage based on which type of demon */
                if (demon instanceof Navec)
                    inflictDamage(demon, fae, "Navec", "Fae");
                else
                    inflictDamage(demon, fae, "Demon", "Fae");
                /* fae enters invincibility state */
                fae.invincible();
            }

        }
    }


    /**
     * Updates the log and the inflictees health
     * @param inflictor
     * @param inflictee
     * @param inflictorString
     * @param inflicteeString
     */
    private void inflictDamage(Object inflictor, Character inflictee, String inflictorString, String inflicteeString) {
        /* downcasts inflictor to character or sinkhole */
        if (inflictor instanceof Character) {
            Character character = (Character) inflictor;
            /* updates health */
            inflictee.setHealth(inflictee.getHealth() - character.getDamagePoints());
            System.out.printf(DAMAGE_LOG_MESSAGE, inflictorString, character.getDamagePoints(), inflicteeString, inflicteeString,
                    inflictee.getHealth(), inflictee.getMaxHealth());

        } else if (inflictor instanceof Sinkhole) {
            Sinkhole sinkhole = (Sinkhole) inflictor;
            /* updates health */
            inflictee.setHealth(inflictee.getHealth() - sinkhole.getDamagePoints());
            System.out.printf(DAMAGE_LOG_MESSAGE, inflictorString, sinkhole.getDamagePoints(), inflicteeString, inflicteeString,
                    inflictee.getHealth(), inflictee.getMaxHealth());
        }

    }

    /**
     * Increases the demons speed
     */
    public void increaseMovement() {
        double factor;
        if (timescale < TIMESCALE_UPPER) {
            /* factor depends on what the current timescale is (in order to reverse change) */
            factor =  (timescale >= 0) ? 1 + TIMESCALE_FACTOR : 1 / (1 - TIMESCALE_FACTOR);
            /* updates demons speed */
            for (Demon demon : demons) {
                if (demon.getCanMove()) {
                    demon.setMovementSpeed(demon.getMovementSpeed() * factor);
                }
            }
            navec.setMovementSpeed(navec.getMovementSpeed() * factor);

            timescale++;
            /* prints message to log */
            System.out.println(SPEED_UP_MESSAGE + timescale);
        }


    }

    /**
     * Decreases the demons speed
     */
    public void decreaseMovement() {
        double factor;
        if (timescale > TIMESCALE_LOWER) {
            /* factor depends on what the current timescale is (in order to reverse change) */
            factor =  (timescale <= 0) ? 1 - TIMESCALE_FACTOR : 1 / (1 + TIMESCALE_FACTOR);
            /* updates demons speed */
            for (Demon demon : demons) {
                if (demon.getCanMove()) {
                    demon.setMovementSpeed(demon.getMovementSpeed() * factor);
                }
            }
            navec.setMovementSpeed(navec.getMovementSpeed() * factor);

            timescale--;
            /* prints message to log */
            System.out.println(SLOW_DOWN_MESSAGE + timescale);
        }



    }


}
