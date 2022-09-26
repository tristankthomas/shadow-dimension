import bagel.*;
public class PassiveDemon extends Demon {
    private final static String DEMON_INVINSIBLE_LEFT = "res/demon/demonInvincibleLeft.png";
    private final static String DEMON_INVINSIBLE_RIGHT = "res/demon/demonInvincibleRight.png";
    private final static String DEMON_LEFT = "res/demon/demonLeft.png";
    private final static String DEMON_RIGHT = "res/demon/demonRight.png";

    private final static int MAX_HEALTH_POINTS = 40;
    private final static int ATTACK_RANGE = 150;

    public PassiveDemon(int xCoord, int yCoord) {
        super(xCoord, yCoord);
        healthPoints = MAX_HEALTH_POINTS;
        maxHealth = MAX_HEALTH_POINTS;
        currentImage = new Image(DEMON_RIGHT);
    }


}
