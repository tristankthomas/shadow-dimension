import bagel.util.Point;
import bagel.util.Rectangle;

/**
 * SWEN20003 Project 2, Semester 2, 2022
 *
 * Obstacle.java: Abstract class representing obstacles including walls, trees, and sinkholes
 *
 * @author Tristan Thomas
 */
public abstract class Obstacle {
    protected Point coords;
    protected Rectangle boundary;

    /**
     * Constructor setting coordinates of obstacle
     * @param coords
     */
    public Obstacle(Point coords) {
        this.coords = coords;
    }

    /* Getter */
    public Rectangle getBoundary() {
        return boundary;
    }

    /**
     * Draws the obstacle
     */
    public abstract void drawObstacle();


}
