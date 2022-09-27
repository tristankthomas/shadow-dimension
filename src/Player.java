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
    private boolean isAttack = false;

    private final int HEALTH_X = 20;
    private final int HEALTH_Y = 25;
    private int cooldownFrameCount = 0;
    private int attackFrameCount = 0;
    private static final int MAX_HEALTH_POINTS = 100;
    private static final int DAMAGE_POINTS = 20;
    private final static int HEALTH_FONT_SIZE = 30;
    private static final double MOVEMENT_SPEED = 2.0;
    private static final int ATTACK_TIME_MS = 1000;
    private static final int IDLE_TIME_MS = 2000;

    public Player() {
        bar = new HealthBar(HEALTH_X, HEALTH_Y, HEALTH_FONT_SIZE);
        currentImage = new Image(FAE_RIGHT);
        maxHealth = MAX_HEALTH_POINTS;
        healthPoints = MAX_HEALTH_POINTS;
        movementSpeed = MOVEMENT_SPEED;
        damagePoints = DAMAGE_POINTS;
    }


    public void move(Input input, World gameWorld) {
        if (input.isDown(Keys.LEFT) && !gameWorld.atBoundary("left", this) &&
                !gameWorld.obstacleIntersect("left", this)) {

            xCoord -= MOVEMENT_SPEED;
            isRight = false;

        }

        if (input.isDown(Keys.RIGHT) && !gameWorld.atBoundary("right", this) &&
                !gameWorld.obstacleIntersect("right", this)) {

            xCoord += MOVEMENT_SPEED;
            isRight = true;

        }

        if (input.isDown(Keys.UP) && !gameWorld.atBoundary("up", this) &&
                !gameWorld.obstacleIntersect("up", this)) {

            yCoord -= MOVEMENT_SPEED;

        }

        if (input.isDown(Keys.DOWN) && !gameWorld.atBoundary("down", this) &&
                !gameWorld.obstacleIntersect("down", this)) {

            yCoord += MOVEMENT_SPEED;

        }
    }



    public boolean getIsCoolDown() {
        return isCooldown;
    }

    public boolean getIsAttack() { return isAttack; }


    /* Draws player based on direction */
    @Override
    public void drawCharacter() {
        /* draws the player in the direction it was last moving */
        if (isAttack) {
            if (isRight) currentImage = new Image(FAE_ATTACK_RIGHT);
            else currentImage = new Image(FAE_ATTACK_LEFT);
        } else {
            if (isRight) currentImage = new Image(FAE_RIGHT);
            else currentImage = new Image(FAE_LEFT);
        }

        super.drawCharacter();

    }

    public void attack(World world) {
        isAttack = true;

        world.faeDemonIntersect();

        if (attackFrameCount / FRAMES_PER_MS == ATTACK_TIME_MS) {
            attackFrameCount = 0;
            isCooldown = true;
            isAttack = false;
        } else {
            attackFrameCount++;
        }

    }

    public void cooldown() {

        if (cooldownFrameCount / FRAMES_PER_MS == IDLE_TIME_MS) {
            cooldownFrameCount = 0;
            isCooldown = false;
        } else {
            cooldownFrameCount++;
        }

    }
}
