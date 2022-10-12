import bagel.*;
import java.util.Random;

/**
 * SWEN20003 Project 2, Semester 2, 2022
 *
 * Navec.java: Class representing Navec
 *
 * @author Tristan Thomas
 */
public class Navec extends Demon {
    private final static String NAVEC_INVINCIBLE_LEFT = "res/navec/navecInvincibleLeft.png";
    private final static String NAVEC_INVINCIBLE_RIGHT = "res/navec/navecInvincibleRight.png";
    private final static String NAVEC_LEFT = "res/navec/navecLeft.png";
    private final static String NAVEC_RIGHT = "res/navec/navecRight.png";
    private final static double LOWER_SPEED = 0.2;
    private final static double UPPER_SPEED = 0.7;
    private static final int DAMAGE_POINTS = 20;
    private Random random = new Random();

    private final static int MAX_HEALTH_POINTS = 80;
    private final static int ATTACK_RANGE = 200;

    /**
     * Constructor initialising all attributes including direction
     * @param xCoord
     * @param yCoord
     */
    public Navec(double xCoord, double yCoord) {
        super(xCoord, yCoord);
        healthPoints = MAX_HEALTH_POINTS;
        maxHealth = MAX_HEALTH_POINTS;
        attackRange = ATTACK_RANGE;
        currentImage = new Image(NAVEC_RIGHT);
        damagePoints = DAMAGE_POINTS;
        movementSpeed = LOWER_SPEED + (random.nextDouble() * (UPPER_SPEED - LOWER_SPEED));
        canMove = true;
        fire = new NavecFire();

        /* randomised direction based on a random integer from 0 to 3 */
        switch (random.nextInt(4)) {
            case 0:
                direction = Direction.RIGHT;
                break;
            case 1:
                direction = Direction.LEFT;
                break;
            case 2:
                direction = Direction.UP;
                break;
            case 3:
                direction = Direction.DOWN;
                break;
        }



    }

    /**
     * Draws Navec based on state and direction
     */
    @Override
    public void drawCharacter() {
        /* draws the player in the direction it was last moving */
        if (isInvincible) {
            if (isRight) currentImage = new Image(NAVEC_INVINCIBLE_RIGHT);
            else currentImage = new Image(NAVEC_INVINCIBLE_LEFT);
        } else {
            if (isRight) currentImage = new Image(NAVEC_RIGHT);
            else currentImage = new Image(NAVEC_LEFT);
        }

        super.drawCharacter();

    }


}
