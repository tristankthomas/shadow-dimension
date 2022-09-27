import bagel.*;
import java.util.Random;

public class AggressiveDemon extends Demon {
    private final static String DEMON_INVINCIBLE_LEFT = "res/demon/demonInvincibleLeft.png";
    private final static String DEMON_INVINCIBLE_RIGHT = "res/demon/demonInvincibleRight.png";
    private final static String DEMON_LEFT = "res/demon/demonLeft.png";
    private final static String DEMON_RIGHT = "res/demon/demonRight.png";
    private final static double LOWER_SPEED = 0.2;
    private final static double UPPER_SPEED = 0.7;
    private static final int DAMAGE_POINTS = 10;

    private final static int MAX_HEALTH_POINTS = 40;
    private final static int ATTACK_RANGE = 150;

    public AggressiveDemon(double xCoord, double yCoord) {
        super(xCoord, yCoord);
        healthPoints = MAX_HEALTH_POINTS;
        maxHealth = MAX_HEALTH_POINTS;
        attackRange = ATTACK_RANGE;
        damagePoints = DAMAGE_POINTS;
        currentImage = new Image(DEMON_RIGHT);
        movementSpeed = LOWER_SPEED + (random.nextDouble() * (UPPER_SPEED - LOWER_SPEED));
        canMove = true;
        fire = new DemonFire();
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
            if (isRight) currentImage = new Image(DEMON_INVINCIBLE_RIGHT);
            else currentImage = new Image(DEMON_INVINCIBLE_LEFT);
        } else {
            if (isRight) currentImage = new Image(DEMON_RIGHT);
            else currentImage = new Image(DEMON_LEFT);
        }

        super.drawCharacter();

    }




}
