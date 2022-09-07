import bagel.*;
import bagel.util.Point;
import bagel.util.Rectangle;

public class Wall {
    private final Image wallImage = new Image("res/wall.png");

    private Point coords;
    private Rectangle rect;

    public Wall(Point coords) {
        this.coords = coords;
        rect = new Rectangle(coords, wallImage.getWidth(), wallImage.getHeight());
    }

    public Rectangle getRect() {
        return rect;
    }

    public void drawWall() {
        wallImage.drawFromTopLeft(coords.x, coords.y);
    }
}
