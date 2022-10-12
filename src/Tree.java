import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

/**
 * SWEN20003 Project 1, Semester 2, 2022
 *
 * Tree.java: Class representing a tree
 *
 * @author Tristan Thomas
 */
public class Tree extends Obstacle {
    private final Image TREE_IMAGE = new Image("res/tree.png");

    /**
     * Constructor to initialise boundary
     * @param coords
     */
    public Tree(Point coords) {
        super(coords);
        boundary = new Rectangle(coords, TREE_IMAGE.getWidth(), TREE_IMAGE.getHeight());
    }

    /**
     * Draws a tree
     */
    public void drawObstacle() {
        TREE_IMAGE.drawFromTopLeft(coords.x, coords.y);
    }

}
