import bagel.DrawOptions;
import bagel.util.Point;
import bagel.Image;
import bagel.util.Rectangle;

/**
 * SWEN20003 Project 2, Semester 2, 2022
 *
 * Fire.java: Abstract class representing fire for both navec and normal demons
 *
 * @author Tristan Thomas
 */
public abstract class Fire {
    private Location location;
    protected Point coords;
    private DrawOptions rotation;
    protected Image currentImage;

    /* Getters and setters */
    public void setLocation(Location location) {
        this.location = location;
    }
    public void setCoords(Point coords) {
        this.coords = coords;
    }

    public Rectangle getBoundary() {
        return new Rectangle(coords.x, coords.y, currentImage.getWidth(), currentImage.getHeight());
    }

    /**
     *  Sets the rotation based on fire location
     */
    public DrawOptions setRotation() {
        switch (location) {
            case TOP_LEFT:
                return rotation.setRotation(0);
            case TOP_RIGHT:
                return rotation.setRotation(Math.PI / 2);
            case BOTTOM_LEFT:
                return rotation.setRotation(-Math.PI / 2);
            case BOTTOM_RIGHT:
                return rotation.setRotation(Math.PI);
        }
        return rotation;
    }

    /**
     * Draws fire based on rotation and position
     */
    public void drawFire() {
        rotation = new DrawOptions();
        setRotation();
        currentImage.drawFromTopLeft(coords.x, coords.y, rotation);

    }



}
