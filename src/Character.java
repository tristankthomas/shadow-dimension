/**
 * SWEN20003 Project 2, Semester 2, 2022
 *
 * World.java: Abstract class representing players including demons and fae
 *
 * @author Tristan Thomas
 */

import bagel.Image;
import bagel.util.Rectangle;
import bagel.util.Point;

public abstract class Character {
    private static final int FRAME_RATE_HZ = 60;
    protected static final double FRAMES_PER_MS = (double) FRAME_RATE_HZ / 1000;
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
    /* keeps track of which direction Fae is facing */
    protected boolean isRight = true;

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

    public void setXCoord(double xCoord) {
        this.xCoord = xCoord;
    }

    public void setYCoord(double yCoord) {
        this.yCoord = yCoord;
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

    public boolean isDead() {
        return healthPoints <= 0;
    }

    public void drawCharacter() {
        bar.updateColour(this);
        bar.drawHealth(this);

        currentImage.drawFromTopLeft(xCoord, yCoord);
    }

    protected void invincible() {
        isInvincible = true;
        if (invincibleFrameCount / FRAMES_PER_MS == INVINCIBLE_TIME_MS) {
            invincibleFrameCount = 0;
            isInvincible = false;
        } else {
            invincibleFrameCount++;
        }
    }

}
