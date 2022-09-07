import bagel.*;
import bagel.util.Point;
import bagel.util.Rectangle;

public class Sinkhole {
    public final static int DAMAGE_POINTS = 30;
    private final Image sinkholeImage = new Image("res/sinkhole.png");
    private Point coords;
    private Rectangle rect;


    public Sinkhole(Point coords) {
        this.coords = coords;
        rect = new Rectangle(coords, sinkholeImage.getWidth(), sinkholeImage.getHeight());
    }



    public Rectangle getRect() {
        return rect;
    }

    public void drawSinkhole() {
        sinkholeImage.drawFromTopLeft(coords.x, coords.y);
    }
}
