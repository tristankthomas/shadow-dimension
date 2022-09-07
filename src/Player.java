import bagel.*;
import bagel.util.Point;
import bagel.util.Rectangle;

public class Player {
    private Image playerImage = new Image("res/faeRight.png");
    public final static int MAX_HEALTH = 100;

    private int health = MAX_HEALTH;
    private int xCoord;
    private int yCoord;
    private Rectangle rect;
    private boolean isRight = true;

    // Constructor
    public Player(int x, int y) {
        this.xCoord = x;
        this.yCoord = y;
        rect = new Rectangle(xCoord, yCoord, playerImage.getWidth(), playerImage.getHeight());
    }

    public Rectangle getRect() {
        return rect;
    }

    // Getters and setters
    public int getHealthPercentage() {
        return (int) ((double) health / (double) MAX_HEALTH * 100.0);
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {

        this.health = (health <= 0) ? 0 : health;

    }

    public void setIsRight(boolean isRight) {
        this.isRight = isRight;
    }

    public int getXCoord() {
        return xCoord;
    }

    public int getYCoord() {
        return yCoord;
    }

    public void setXCoord(int xCoord) {
        this.xCoord = xCoord;
    }

    public void setYCoord(int yCoord) {
        this.yCoord = yCoord;
    }

    // Draws player based on direction
    public void drawPlayer() {
        if (isRight) playerImage = new Image("res/faeRight.png");
        else playerImage = new Image("res/faeLeft.png");

        rect = new Rectangle(xCoord, yCoord, playerImage.getWidth(), playerImage.getHeight());
        playerImage.drawFromTopLeft(xCoord, yCoord);
    }
}
