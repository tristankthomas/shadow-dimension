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
    private final static String FAE_RIGHT = "res/fae/faeRight.png";
    private final static String FAE_LEFT = "res/fae/faeLeft.png";
    private final static String FAE_ATTACK_RIGHT = "res/fae/faeAttackRight.png";
    private final static String FAE_ATTACK_LEFT = "res/fae/faeAttackLeft.png";
    private boolean isCooldown = false;

    private final int HEALTH_X = 20;
    private final int HEALTH_Y = 25;
    private static final State INITIAL_STATE = State.IDLE;
    private static final int MAX_HEALTH_POINTS = 100;
    private static final int DAMAGE_POINTS = 20;
    private final static int HEALTH_FONT_SIZE = 30;
    private static final int MOVEMENT_SPEED = 2;
    private static final int ATTACK_TIME_MS = 1000;
    private static final int IDLE_TIME_MS = 2000;

    public Player() {
        bar = new HealthBar(HEALTH_X, HEALTH_Y, HEALTH_FONT_SIZE);
        currentImage = new Image(FAE_RIGHT);
        maxHealth = MAX_HEALTH_POINTS;
        healthPoints = MAX_HEALTH_POINTS;
        state = INITIAL_STATE;
    }


    public void move(Input input, World gameWorld) {
        if (input.isDown(Keys.LEFT) && !gameWorld.atBoundary("left") &&
                !gameWorld.obstacleIntersect("left")) {

            xCoord -= MOVEMENT_SPEED;
            isRight = false;

        }

        if (input.isDown(Keys.RIGHT) && !gameWorld.atBoundary("right") &&
                !gameWorld.obstacleIntersect("right")) {

            xCoord += MOVEMENT_SPEED;
            isRight = true;

        }

        if (input.isDown(Keys.UP) && !gameWorld.atBoundary("up") &&
                !gameWorld.obstacleIntersect("up")) {

            yCoord -= MOVEMENT_SPEED;

        }

        if (input.isDown(Keys.DOWN) && !gameWorld.atBoundary("down") &&
                !gameWorld.obstacleIntersect("down")) {

            yCoord += MOVEMENT_SPEED;

        }
    }

    public static int getAttackTimeMs() {
        return ATTACK_TIME_MS;
    }

    public static int getDamagePoints() {
        return DAMAGE_POINTS;
    }

    public static int getIdleTimeMs() {
        return IDLE_TIME_MS;
    }

    public boolean getIsCoolDown() {
        return isCooldown;
    }

    public void setIsCoolDown(boolean isCoolDown) {
        this.isCooldown = isCoolDown;
    }


    /* Draws player based on direction */
    @Override
    public void drawCharacter() {
        /* draws the player in the direction it was last moving */
        switch (state) {
            case IDLE:
                if (isRight) currentImage = new Image(FAE_RIGHT);
                else currentImage = new Image(FAE_LEFT);
                break;
            case ATTACK:
                if (isRight) currentImage = new Image(FAE_ATTACK_RIGHT);
                else currentImage = new Image(FAE_ATTACK_LEFT);
                break;
        }

        super.drawCharacter();

    }

    public void attack(World world) {
        state = State.ATTACK;
        world.demonIntersect();

        if (frameCount / FRAMES_PER_MS == ATTACK_TIME_MS) {
            frameCount = 0;
            isCooldown = true;
            state = State.IDLE;
        } else {
            frameCount++;
        }

    }

    public void cooldown() {

        if (frameCount / FRAMES_PER_MS == IDLE_TIME_MS) {
            frameCount = 0;
            isCooldown = false;
        } else {
            frameCount++;
        }

    }
}
