public class Demon extends Character {

    private static final double SPEED_FACTOR = 0.5;
    private static final int TIMESCALE_LOWER = -3;
    private static final int TIMESCALE_UPPER = 3;
    private final static int HEALTH_FONT_SIZE = 15;
    private static final int HEALTH_Y_OFFSET = 6;
    private static final State INITIAL_STATE = State.ATTACK;
    protected boolean canMove = false;
    protected String direction;

    public Demon(double xCoord, double yCoord) {
       this.xCoord = xCoord;
       this.yCoord = yCoord;
       bar = new HealthBar(xCoord, yCoord - HEALTH_Y_OFFSET, HEALTH_FONT_SIZE);
       state = INITIAL_STATE;
    }

    public boolean getCanMove() {
        return canMove;
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


//            - healthX: int
//- healthY: int

}
