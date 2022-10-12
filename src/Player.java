import bagel.*;

/**
 * SWEN20003 Project 2, Semester 2, 2022
 *
 * Player.java: Class representing the player
 *
 * @author Tristan Thomas
 */
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

    /**
     * Constructor to initialise player attributes
     * @param xCoord
     * @param yCoord
     */
    public Player(double xCoord, double yCoord) {
        super(xCoord, yCoord);
        bar = new HealthBar(HEALTH_X, HEALTH_Y, HEALTH_FONT_SIZE);
        currentImage = new Image(FAE_RIGHT);
        maxHealth = MAX_HEALTH_POINTS;
        healthPoints = MAX_HEALTH_POINTS;
        movementSpeed = MOVEMENT_SPEED;
        damagePoints = DAMAGE_POINTS;
    }

    /* Getters */
    public boolean getIsCoolDown() {
        return isCooldown;
    }

    public boolean getIsAttack() { return isAttack; }


    /**
     * Allows the movement of fae using keyboard input
     * @param input
     * @param gameWorld
     */
    public void move(Input input, World gameWorld) {
        /* only moves is obstacle or game boundary is not present */
        if (input.isDown(Keys.LEFT) && !gameWorld.atBoundary(Direction.LEFT, this) &&
                !gameWorld.obstacleIntersect(Direction.LEFT, this)) {

            xCoord -= MOVEMENT_SPEED;
            isRight = false;

        }

        if (input.isDown(Keys.RIGHT) && !gameWorld.atBoundary(Direction.RIGHT, this) &&
                !gameWorld.obstacleIntersect(Direction.RIGHT, this)) {

            xCoord += MOVEMENT_SPEED;
            isRight = true;

        }

        if (input.isDown(Keys.UP) && !gameWorld.atBoundary(Direction.UP, this) &&
                !gameWorld.obstacleIntersect(Direction.UP, this)) {

            yCoord -= MOVEMENT_SPEED;

        }

        if (input.isDown(Keys.DOWN) && !gameWorld.atBoundary(Direction.DOWN, this) &&
                !gameWorld.obstacleIntersect(Direction.DOWN, this)) {

            yCoord += MOVEMENT_SPEED;

        }
    }


    /**
     * Draws player based on state and direction
     */
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

    /**
     * Allows for the player attack mode to be entered and counts down attack time left
     * @param world
     * @param level
     */
    public void attack(World world, int level) {
        isAttack = true;

        /* only attacks demons in level 1 (attack mode can still be entered in level 0) */
        if (level == 1) world.faeDemonIntersect();

        /* counts down how long is left in attack mode */
        if (attackFrameCount / FRAMES_PER_MS == ATTACK_TIME_MS) {
            /* attack mode deactivated */
            attackFrameCount = 0;
            isCooldown = true;
            isAttack = false;
        } else {
            attackFrameCount++;
        }

    }

    /**
     * After attack mode is finished cooldown period begins (same timing mechanism as attack)
     */
    public void cooldown() {

        if (cooldownFrameCount / FRAMES_PER_MS == IDLE_TIME_MS) {
            cooldownFrameCount = 0;
            isCooldown = false;
        } else {
            cooldownFrameCount++;
        }

    }
}
