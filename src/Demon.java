import java.util.Random;
import bagel.util.Point;

/**
 * SWEN20003 Project 2, Semester 2, 2022
 *
 * Demon.java: Class representing demons including navec, aggressive demons, and passive demons
 *
 * @author Tristan Thomas
 */
public class Demon extends Character {
    private final static int HEALTH_FONT_SIZE = 15;
    protected Random random = new Random();
    private static final int HEALTH_Y_OFFSET = 6;
    protected Fire fire;
    protected boolean canMove = false;
    protected Direction direction;
    protected int attackRange;

    /**
     * Constructor for demon class
     * @param xCoord
     * @param yCoord
     */
    public Demon(double xCoord, double yCoord) {
        super(xCoord, yCoord);
       this.xCoord = xCoord;
       this.yCoord = yCoord;
       bar = new HealthBar(xCoord, yCoord - HEALTH_Y_OFFSET, HEALTH_FONT_SIZE);
    }

    /* Getters */
    public boolean getCanMove() {
        return canMove;
    }
    public int getAttackRange() {
        return attackRange;
    }

    /**
     * Updates the demons coordinates and direction
     * @param gameWorld
     */
    public void move(World gameWorld) {
        if (gameWorld.demonIntersect(this)) {
            switchDirection();
        }
        /* changes coordinates based on direction */
        switch (direction) {
            case RIGHT:
                xCoord += movementSpeed;
                isRight = true;
                break;
            case LEFT:
                xCoord -= movementSpeed;
                isRight = false;
                break;
            case UP:
                yCoord -= movementSpeed;
                break;
            case DOWN:
                yCoord += movementSpeed;
                break;
        }
        /* updates health bar coords */
        bar.setXCoord(xCoord);
        bar.setYCoord(yCoord - HEALTH_Y_OFFSET);


    }

    /**
     * Changes the demons direction depending on which way it was facing prior
     */
    public void switchDirection() {

        switch (direction) {
            case RIGHT:
                direction = Direction.LEFT;
                isRight = false;
                break;
            case LEFT:
                direction = Direction.RIGHT;
                isRight = true;
                break;
            case UP:
                direction = Direction.DOWN;
                break;
            case DOWN:
                direction = Direction.UP;
                break;
        }


    }



    /**
     * Allows demon to attack with fire
     * @param gameWorld
     */
    public void attack(World gameWorld) {
        /* calculates where the fire should be based on faes position */
        Location location = gameWorld.fireLocation(this);
        Point point;
        fire.setLocation(location);
        /* sets the fire coordinates relative to the demon */
        switch (location) {
            case TOP_LEFT:
                point = getBoundary().topLeft();
                fire.setCoords(new Point(point.x - fire.currentImage.getWidth(), point.y - fire.currentImage.getHeight()));
                break;
            case TOP_RIGHT:
                point = getBoundary().topRight();
                fire.setCoords(new Point(point.x, point.y - fire.currentImage.getHeight()));
                break;
            case BOTTOM_LEFT:
                point = getBoundary().bottomLeft();
                fire.setCoords(new Point(point.x - fire.currentImage.getWidth(), point.y));
                break;
            case BOTTOM_RIGHT:
                point = getBoundary().bottomRight();
                fire.setCoords(point);
                break;
        }

        fire.drawFire();
        /* check if fae is intersecting with the fire */
        gameWorld.flameIntersect(this);

    }




}
