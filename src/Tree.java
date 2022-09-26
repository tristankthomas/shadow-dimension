import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

public class Tree extends Obstacle {
    private final Image TREE_IMAGE = new Image("res/tree.png");

    public Tree(Point coords) {
        super(coords);
        boundary = new Rectangle(coords, TREE_IMAGE.getWidth(), TREE_IMAGE.getHeight());
    }


    /* Draws a wall */
    public void drawObstacle() {
        TREE_IMAGE.drawFromTopLeft(coords.x, coords.y);
    }

}
