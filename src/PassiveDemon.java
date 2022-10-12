import bagel.*;

/**
 * SWEN20003 Project 2, Semester 2, 2022
 *
 * PassiveDemon.java: Class representing the passive demon character
 *
 * @author Tristan Thomas
 */
public class PassiveDemon extends Demon {
    private final static String DEMON_INVINCIBLE_LEFT = "res/demon/demonInvincibleLeft.png";
    private final static String DEMON_INVINCIBLE_RIGHT = "res/demon/demonInvincibleRight.png";
    private final static String DEMON_LEFT = "res/demon/demonLeft.png";
    private final static String DEMON_RIGHT = "res/demon/demonRight.png";
    private static final int DAMAGE_POINTS = 10;
    private final static int MAX_HEALTH_POINTS = 40;
    private final static int ATTACK_RANGE = 150;

    /**
     * Constructor that initialises passive demons attributes
     * @param xCoord
     * @param yCoord
     */
    public PassiveDemon(double xCoord, double yCoord) {
        super(xCoord, yCoord);
        healthPoints = MAX_HEALTH_POINTS;
        maxHealth = MAX_HEALTH_POINTS;
        attackRange = ATTACK_RANGE;
        damagePoints = DAMAGE_POINTS;
        currentImage = new Image(DEMON_RIGHT);
        /* randomised direction demon is facing */
        isRight = (random.nextBoolean()) ? true : false;
        fire = new DemonFire();
    }

    /**
     * Draws the passive demon based on state and direction
     */
    @Override
    public void drawCharacter() {
        /* draws the player in the direction it was last moving */
        if (isInvincible) {
            if (isRight) currentImage = new Image(DEMON_INVINCIBLE_RIGHT);
            else currentImage = new Image(DEMON_INVINCIBLE_LEFT);
        } else {
            if (isRight) currentImage = new Image(DEMON_RIGHT);
            else currentImage = new Image(DEMON_LEFT);
        }

        super.drawCharacter();

    }


}
