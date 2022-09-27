import bagel.Image;

public class NavecFire extends Fire {
    private static final int DAMAGE_POINTS = 20;
    private static final String NAVEC_FIRE_IMAGE = "res/navec/navecFire.png";

    public NavecFire() {
        currentImage = new Image(NAVEC_FIRE_IMAGE);
    }

}
