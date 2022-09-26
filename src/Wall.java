/**
 * SWEN20003 Project 1, Semester 2, 2022
 *
 * Wall.java: Class representing a wall
 *
 * @author Tristan Thomas
 */

import bagel.*;
import bagel.util.Point;
import bagel.util.Rectangle;

public class Wall extends Obstacle {
    private final Image WALL_IMAGE = new Image("res/wall.png");

    public Wall(Point coords) {
        super(coords);
        boundary = new Rectangle(coords, WALL_IMAGE.getWidth(), WALL_IMAGE.getHeight());
    }


    /* Draws a wall */
    public void drawObstacle() {
        WALL_IMAGE.drawFromTopLeft(coords.x, coords.y);
    }
}
