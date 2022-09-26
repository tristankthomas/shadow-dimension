/**
 * SWEN20003 Project 2, Semester 2, 2022
 *
 * Player.java: Class representing the player
 *
 * @author Tristan Thomas
 */

import bagel.*;
import bagel.util.Rectangle;

public class Player extends Character {
    private Image playerImage = new Image("res/fae/faeRight.png");
    private final static int MAX_HEALTH = 100;
    private final int HEALTH_X = 20;
    private final int HEALTH_Y = 25;

    private int health = MAX_HEALTH;
    private int xCoord;
    private int yCoord;
    private Rectangle rect;
    private final static int STEP_SIZE = 2;

    // keeps track of which direction Fae is facing
    private boolean isRight = true;

    public Player() {
        bar = new HealthBar(HEALTH_X, HEALTH_Y);
    }

    public Rectangle getRect() {
        return new Rectangle(xCoord, yCoord, playerImage.getWidth(), playerImage.getHeight());
    }

    /* Getters and setters */
    public int getHealthPercentage() {
        /* casts to double to stop floor division then casts back to int */
        return (int) ((double) health / MAX_HEALTH * 100.0);
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return MAX_HEALTH;
    }

    public void setHealth(int health) {
        // if health goes negative sets health to 0
        this.health = (health <= 0) ? 0 : health;

    }

    public void setIsRight(boolean isRight) {
        this.isRight = isRight;
    }

    public int getXCoord() {
        return xCoord;
    }

    public int getYCoord() {
        return yCoord;
    }

    public void setXCoord(int xCoord) {
        this.xCoord = xCoord;
    }

    public void setYCoord(int yCoord) {
        this.yCoord = yCoord;
    }

    public HealthBar getHealthBar() {
        return bar;
    }

    public void move(Input input, World gameWorld) {
        if (input.isDown(Keys.LEFT) && !gameWorld.atBoundary("left") &&
                !gameWorld.wallIntersect("left")) {

            gameWorld.getFae().setXCoord(xCoord - STEP_SIZE);
            gameWorld.getFae().setIsRight(false);

        }

        if (input.isDown(Keys.RIGHT) && !gameWorld.atBoundary("right") &&
                !gameWorld.wallIntersect("right")) {

            gameWorld.getFae().setXCoord(xCoord + STEP_SIZE);
            gameWorld.getFae().setIsRight(true);

        }

        if (input.isDown(Keys.UP) && !gameWorld.atBoundary("up") &&
                !gameWorld.wallIntersect("up")) {

            gameWorld.getFae().setYCoord(yCoord - STEP_SIZE);

        }

        if (input.isDown(Keys.DOWN) && !gameWorld.atBoundary("down") &&
                !gameWorld.wallIntersect("down")) {

            gameWorld.getFae().setYCoord(yCoord + STEP_SIZE);

        }
    }
    /* Draws player based on direction */
    public void drawPlayer() {
        /* draws the player in the direction it was last moving */
        if (isRight) playerImage = new Image("res/fae/faeRight.png");
        else playerImage = new Image("res/fae/faeLeft.png");

        rect = new Rectangle(xCoord, yCoord, playerImage.getWidth(), playerImage.getHeight());
        bar.drawHealth(this);
        playerImage.drawFromTopLeft(xCoord, yCoord);

    }
}
