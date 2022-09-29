import java.util.Random;
import bagel.util.Point;

public class Demon extends Character {
    private final static int HEALTH_FONT_SIZE = 15;
    protected Random random = new Random();
    private static final int HEALTH_Y_OFFSET = 6;
    protected Fire fire;
    protected boolean canMove = false;
    protected String direction;
    protected int attackRange;

    public Demon(double xCoord, double yCoord) {
       this.xCoord = xCoord;
       this.yCoord = yCoord;
       bar = new HealthBar(xCoord, yCoord - HEALTH_Y_OFFSET, HEALTH_FONT_SIZE);
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

    public static void increaseTimescale(World gameWorld) {


        gameWorld.increaseMovement();

    }

    public static void decreaseTimescale(World gameWorld) {


        gameWorld.decreaseMovement();
    }




    public void attack(World gameWorld) {
        Location location = gameWorld.fireLocation(this);
        Point point;
        fire.setLocation(location);
        switch (location) {
            case TOP_LEFT:
                point = getBoundary().topLeft();
                fire.setCoords(new Point(point.x - fire.currentImage.getWidth(), point.y - fire.currentImage.getHeight()));
                break;
            case TOP_RIGHT:
                point = getBoundary().topRight();
                fire.setCoords(new Point(point.x, point.y - fire.currentImage.getHeight()));
                break;
            case BOTTOM_LEFT:
                point = getBoundary().bottomLeft();
                fire.setCoords(new Point(point.x - fire.currentImage.getWidth(), point.y));
                break;
            case BOTTOM_RIGHT:
                point = getBoundary().bottomRight();
                fire.setCoords(point);
                break;
        }
        fire.drawFire();
        gameWorld.flameIntersect(this);
    }




}
