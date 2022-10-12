import bagel.Image;
import bagel.util.Rectangle;
import bagel.util.Point;

/**
 * SWEN20003 Project 2, Semester 2, 2022
 *
 * Character.java: Abstract class representing characters including demons and fae
 *
 * @author Tristan Thomas
 */
public abstract class Character {
    private static final int FRAME_RATE_HZ = 60;
    private static final int SEC_TO_MS = 1000;
    protected static final double FRAMES_PER_MS = (double) FRAME_RATE_HZ / SEC_TO_MS;
    private static final int INVINCIBLE_TIME_MS = 3000;
    protected boolean isInvincible = false;
    protected double xCoord;
    protected double yCoord;
    protected int damagePoints;
    protected int healthPoints;
    protected int invincibleFrameCount = 0;
    protected int maxHealth;
    protected double movementSpeed;
    protected HealthBar bar;
    protected Image currentImage;
    /* keeps track of which direction the character is facing */
    protected boolean isRight = true;

    /**
     * Constructor for a character
     * @param xCoord
     * @param yCoord
     */
    public Character(double xCoord, double yCoord) {
        this.xCoord = xCoord;
        this.yCoord = yCoord;
    }

    /* Getters and setters */
    public double getXCoord() {
        return xCoord;
    }

    public double getYCoord() {
        return yCoord;
    }
    public Image getCurrentImage() {
        return currentImage;
    }

    public int getDamagePoints() {
        return damagePoints;
    }

    public void setMovementSpeed(double movementSpeed) {
        this.movementSpeed = movementSpeed;
    }

    public double getMovementSpeed() {
        return movementSpeed;
    }

    public int getHealth() {
        return healthPoints;
    }
    public Rectangle getBoundary() {
        return new Rectangle(xCoord, yCoord, currentImage.getWidth(), currentImage.getHeight());
    }
    public int getMaxHealth() {
        return maxHealth;
    }

    public int getHealthPercentage() {
        /* casts to double to stop floor division then casts back to int */
        return (int) ((double) healthPoints / maxHealth * 100.0);
    }

    public Point getCentredCoord() {
        return new Point(xCoord + (currentImage.getWidth() / 2), yCoord + (currentImage.getHeight() / 2));
    }

    public void setHealth(int health) {
        /* if health goes negative sets health to 0 */
        this.healthPoints = (health <= 0) ? 0 : health;

    }

    public static double getFramesPerMs() {
        return FRAMES_PER_MS;
    }

    /**
     * Returns whether a character is dead or not
     * @return
     */
    public boolean isDead() {
        return healthPoints <= 0;
    }

    /**
     * Draws character by current image
     */
    public void drawCharacter() {
        bar.updateColour(this);
        bar.drawHealth(this);

        currentImage.drawFromTopLeft(xCoord, yCoord);
    }

    /**
     * When character gets attacked invincible method is used
     */
    protected void invincible() {
        isInvincible = true;
        if (invincibleFrameCount / FRAMES_PER_MS == INVINCIBLE_TIME_MS) {
            /* if invincible time is up character no longer invincible */
            invincibleFrameCount = 0;
            isInvincible = false;
        } else {
            invincibleFrameCount++;
        }
    }

}
