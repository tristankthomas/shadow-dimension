/**
 * SWEN20003 Project 2, Semester 2, 2022
 *
 * World.java: Abstract class representing players including demons and fae
 *
 * @author Tristan Thomas
 */

public abstract class Character {
    private static final int FRAME_RATE_HZ = 60;
    private static final double FRAMES_PER_MS = (double) FRAME_RATE_HZ / 1000;

    protected HealthBar bar;

    public static double getFramesPerMs() {
        return FRAMES_PER_MS;
    }

}
