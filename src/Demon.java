import java.util.Random;

public class Demon extends Character {

    private static final double SPEED_FACTOR = 0.5;
    private static final int TIMESCALE_LOWER = -3;
    private static final int TIMESCALE_UPPER = 3;
    private final static int HEALTH_FONT_SIZE = 15;
    protected Random random = new Random();
    private static final int HEALTH_Y_OFFSET = 6;
    private static final State INITIAL_STATE = State.ATTACK;
    protected Fire fire;
    protected boolean canMove = false;
    protected String direction;
    protected int attackRange;

    public Demon(double xCoord, double yCoord) {
       this.xCoord = xCoord;
       this.yCoord = yCoord;
       bar = new HealthBar(xCoord, yCoord - HEALTH_Y_OFFSET, HEALTH_FONT_SIZE);
       state = INITIAL_STATE;
    }

    public boolean getCanMove() {
        return canMove;
    }
    public int getAttackRange() {
        return attackRange;
    }

    public void move(World gameWorld) {
        if (gameWorld.demonIntersect(this)) {
            switchDirection();
        }
        switch (direction) {
            case "right":
                xCoord += movementSpeed;
                isRight = true;
                break;
            case "left":
                xCoord -= movementSpeed;
                isRight = false;
                break;
            case "up":
                yCoord -= movementSpeed;
                break;
            case "down":
                yCoord += movementSpeed;
                break;
        }
        bar.setXCoord(xCoord);
        bar.setYCoord(yCoord - HEALTH_Y_OFFSET);


    }

    public void switchDirection() {

        switch (direction) {
            case "right":
                direction = "left";
                isRight = false;
                break;
            case "left":
                direction = "right";
                isRight = true;
                break;
            case "up":
                direction = "down";
                break;
            case "down":
                direction = "up";
                break;
        }


    }

    public void attack(World gameWorld) {
        Location location = gameWorld.fireLocation(this);
        fire.setLocation(location);
        switch (location) {
            case TOP_LEFT:
                fire.setCoords(getBoundary().topLeft());
                break;
            case TOP_RIGHT:
                fire.setCoords(getBoundary().topRight());
                break;
            case BOTTOM_LEFT:
                fire.setCoords(getBoundary().bottomLeft());
                break;
            case BOTTOM_RIGHT:
                fire.setCoords(getBoundary().bottomRight());
                break;
        }
        fire.drawFire();

    }




}
