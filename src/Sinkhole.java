/**
 * SWEN20003 Project 1, Semester 2, 2022
 *
 * Sinkhole.java: Class representing a sinkhole
 *
 * @author Tristan Thomas
 */

import bagel.*;
import bagel.util.Point;
import bagel.util.Rectangle;

public class Sinkhole extends Obstacle {
    private final static int DAMAGE_POINTS = 30;
    private final Image SINKHOLE_IMAGE = new Image("res/sinkhole.png");


    public Sinkhole(Point coords) {
        super(coords);
        super.boundary = new Rectangle(coords, SINKHOLE_IMAGE.getWidth(), SINKHOLE_IMAGE.getHeight());
    }

    /* Getters */

    public static int getDamagePoints() {
        return DAMAGE_POINTS;
    }

    /* Draws a sinkhole */
    public void drawObstacle() {
        SINKHOLE_IMAGE.drawFromTopLeft(coords.x, coords.y);
    }
}
