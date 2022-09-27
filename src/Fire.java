import bagel.DrawOptions;
import bagel.util.Point;
import bagel.Image;
import bagel.util.Rectangle;

public abstract class Fire {
    private Location location;
    protected Point coords;
    private DrawOptions rotation;
    protected Image currentImage;

    public void drawFire() {
        rotation = new DrawOptions();
        setRotation();
        currentImage.drawFromTopLeft(coords.x, coords.y, rotation);

    }

    public void setLocation(Location location) {
        this.location = location;
    }
    public void setCoords(Point coords) {
        this.coords = coords;
    }

    public Rectangle getBoundary() {
        return new Rectangle(coords.x, coords.y, currentImage.getWidth(), currentImage.getHeight());
    }

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

}
