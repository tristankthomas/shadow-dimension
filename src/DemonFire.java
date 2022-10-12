import bagel.Image;

/**
 * SWEN20003 Project 2, Semester 2, 2022
 *
 * DemonFire.java: Class representing demons fire
 *
 * @author Tristan Thomas
 */
public class DemonFire extends Fire {
    private static final String DEMON_FIRE_IMAGE = "res/demon/demonFire.png";

    /**
     * Constructor to set the demon fires current image
     */
    public DemonFire() {
        currentImage = new Image(DEMON_FIRE_IMAGE);
    }

}
