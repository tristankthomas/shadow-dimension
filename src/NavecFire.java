import bagel.Image;

/**
 * SWEN20003 Project 2, Semester 2, 2022
 *
 * NavecFire.java: Class representing navecs fire
 *
 * @author Tristan Thomas
 */
public class NavecFire extends Fire {
    private static final String NAVEC_FIRE_IMAGE = "res/navec/navecFire.png";

    /**
     * Constructor to set Navecs fire image
     */
    public NavecFire() {
        currentImage = new Image(NAVEC_FIRE_IMAGE);
    }

}
