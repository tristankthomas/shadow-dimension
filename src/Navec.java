import bagel.*;
import java.util.Random;

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
        switch (random.nextInt(4)) {
            case 0:
                direction = "right";
                break;
            case 1:
                direction = "left";
                break;
            case 2:
                direction = "up";
                break;
            case 3:
                direction = "down";
                break;
        }



    }

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
