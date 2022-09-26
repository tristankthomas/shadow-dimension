import bagel.*;
import bagel.util.Vector2;

public class Navec extends Demon {
    private final static String NAVEC_INVINSIBLE_LEFT = "res/navec/navecInvincibleLeft.png";
    private final static String NAVEC_INVINSIBLE_RIGHT = "res/navec/navecInvincibleRight.png";
    private final static String NAVEC_LEFT = "res/navec/navecLeft.png";
    private final static String NAVEC_RIGHT = "res/navec/navecRight.png";

    private final static int MAX_HEALTH_POINTS = 80;
    private final static int ATTACK_RANGE = 200;

    private int movementSpeed;
    private Vector2 direction;

    public Navec(int xCoord, int yCoord) {
        super(xCoord, yCoord);
        healthPoints = MAX_HEALTH_POINTS;
        maxHealth = MAX_HEALTH_POINTS;
        currentImage = new Image(NAVEC_RIGHT);
    }


}
