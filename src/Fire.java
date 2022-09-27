import bagel.util.Point;
import bagel.Image;
public abstract class Fire {
    private Location location;
    protected Point coords;
    protected Image currentImage;

    public void drawFire() {
        currentImage.drawFromTopLeft(coords.x, coords.y);
    }

    public void setLocation(Location location) {
        this.location = location;
    }
    public void setCoords(Point coords) {
        this.coords = coords;
    }
}
