/**
 * SWEN20003 Project 2, Semester 2, 2022
 *
 * World.java: Abstract class representing players including demons and fae
 *
 * @author Tristan Thomas
 */

import bagel.Image;
import bagel.util.Rectangle;

public abstract class Character {
    private static final int FRAME_RATE_HZ = 60;
    protected static final double FRAMES_PER_MS = (double) FRAME_RATE_HZ / 1000;
    private static final int INVINCIBLE_TIME_MS = 2000;
    protected int xCoord;
    protected int yCoord;
    protected int healthPoints;
    protected int frameCount = 0;
    protected int maxHealth;
    protected HealthBar bar;
    protected Image currentImage;
    protected State state;
    /* keeps track of which direction Fae is facing */
    protected boolean isRight = true;

    public int getXCoord() {
        return xCoord;
    }

    public int getYCoord() {
        return yCoord;
    }

    public void setXCoord(int xCoord) {
        this.xCoord = xCoord;
    }

    public void setYCoord(int yCoord) {
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

    public void setHealth(int health) {
        /* if health goes negative sets health to 0 */
        this.healthPoints = (health <= 0) ? 0 : health;

    }

    protected HealthBar getHealthBar() {
        return bar;
    }

    public static double getFramesPerMs() {
        return FRAMES_PER_MS;
    }

    public boolean isDead() {
        return healthPoints <= 0;
    }

    public void drawCharacter() {

        bar.drawHealth(this);
        currentImage.drawFromTopLeft(xCoord, yCoord);
    }

    protected void invincible() {
        state = State.INVISIBLE;
        if (frameCount / FRAMES_PER_MS == INVINCIBLE_TIME_MS) {
            frameCount = 0;
            state = State.ATTACK;
        } else {
            frameCount++;
        }
    }

}
