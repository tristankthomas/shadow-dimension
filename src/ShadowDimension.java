/**
 * SWEN20003 Project 1, Semester 2, 2022
 *
 * ShadowDimension.java: Main Shadow Dimension class used to run game
 *
 * @author Tristan Thomas
 */

import bagel.*;
import bagel.util.Colour;
import bagel.util.Point;

public class ShadowDimension extends AbstractGame {
    /* game window attributes */
    private final static int WINDOW_WIDTH = 1024;
    private final static int WINDOW_HEIGHT = 768;
    private final static String GAME_TITLE = "SHADOW DIMENSION";
    private final static String WIN_MESSAGE = "CONGRATULATIONS!";
    private final static String LOSE_MESSAGE = "GAME OVER!";
    private final Image BACKGROUND_IMAGE = new Image("res/background0.png");

    /* health bar attributes */
    private final static int LOW_HEALTH = 35;
    private final static int MEDIUM_HEALTH = 65;
    private final static Colour GREEN = new Colour(0,0.8,0.2);
    private final static Colour ORANGE = new Colour(0.9,0.6,0);
    private final static Colour RED = new Colour(1,0,0);
    private Colour healthColour = GREEN;
    private DrawOptions healthOptions = new DrawOptions();

    /* font definitions */
    private final static int DEFAULT_FONT_SIZE = 75;
    private final static int INSTRUCTION_FONT_SIZE = 40;
    private final static int HEALTH_FONT_SIZE = 30;
    private final Font DEFAULT_FONT = new Font("res/frostbite.ttf", DEFAULT_FONT_SIZE);
    private final Font INSTRUCTION_FONT = new Font("res/frostbite.ttf", INSTRUCTION_FONT_SIZE);
    private final Font HEALTH_FONT = new Font("res/frostbite.ttf", HEALTH_FONT_SIZE);
    /* text coordinates */
    private final int PORTAL_X = 950;
    private final int PORTAL_Y = 670;
    private final int TITLE_X = 260;
    private final int TITLE_Y = 250;
    private final int INSTRUCTION_X = 350;
    private final int INSTRUCTION1_Y = 440;
    private final int INSTRUCTION2_Y = 490;
    private final int HEALTH_X = 20;
    private final int HEALTH_Y = 25;

    /* World object representing level 0 of Shadow Dimension */
    private World level0;
    private final static int STEP_SIZE = 2;
    private boolean gameStart = false;


    public ShadowDimension() {
        super(WINDOW_WIDTH, WINDOW_HEIGHT, GAME_TITLE);
    }

    /* Entry point for the program */
    public static void main(String[] args) {
        ShadowDimension game = new ShadowDimension();
        game.run();
    }

    /* Calculates the centre coordinates for a given string (top left of string) */
    public Point getCentredCoord(String text) {
        return new Point(Window.getWidth() / 2 - DEFAULT_FONT.getWidth(text) / 2,
                Window.getHeight() / 2 + (double) DEFAULT_FONT_SIZE / 2);
    }


    /* Performs a state update at the refresh rate of screen */
    @Override
    protected void update(Input input) {

        if (input.wasPressed(Keys.ESCAPE))
            /* closes window */
            Window.close();
        else if (!gameStart) {
            /* waits for game to start */
            if (input.wasPressed(Keys.SPACE)) {
                /* game has started */
                gameStart = true;
                /* creates a new world layout (level 0) */
                level0 = new World("res/level0.csv");
            }
            /* title screen */
            DEFAULT_FONT.drawString(GAME_TITLE, TITLE_X, TITLE_Y);
            INSTRUCTION_FONT.drawString("PRESS SPACE TO START", INSTRUCTION_X, INSTRUCTION1_Y);
            INSTRUCTION_FONT.drawString("USE ARROW KEYS TO FIND GATE", INSTRUCTION_X, INSTRUCTION2_Y);

        } else if (level0.getFae().getXCoord() >= PORTAL_X && level0.getFae().getYCoord() >= PORTAL_Y) {
            /* win screen */
            Point winTextCoord = getCentredCoord(WIN_MESSAGE);
            DEFAULT_FONT.drawString(WIN_MESSAGE, winTextCoord.x, winTextCoord.y);

        } else if (level0.getFae().getHealth() <= 0) {
            /* lose screen */
            Point loseTextCoord = getCentredCoord(LOSE_MESSAGE);
            DEFAULT_FONT.drawString(LOSE_MESSAGE, loseTextCoord.x, loseTextCoord.y);

        } else {
            /* moves player if not at a boundary and arrow keys are pressed */
            if (input.isDown(Keys.LEFT) && !level0.atBoundary("left") &&
                    !level0.wallIntersect("left")) {

                level0.getFae().setXCoord(level0.getFae().getXCoord() - STEP_SIZE);
                level0.getFae().setIsRight(false);

            }

            if (input.isDown(Keys.RIGHT) && !level0.atBoundary("right") &&
                    !level0.wallIntersect("right")) {

                level0.getFae().setXCoord(level0.getFae().getXCoord() + STEP_SIZE);
                level0.getFae().setIsRight(true);

            }

            if (input.isDown(Keys.UP) && !level0.atBoundary("up") &&
                    !level0.wallIntersect("up")) {

                level0.getFae().setYCoord(level0.getFae().getYCoord() - STEP_SIZE);

            }

            if (input.isDown(Keys.DOWN) && !level0.atBoundary("down") &&
                    !level0.wallIntersect("down")) {

                level0.getFae().setYCoord(level0.getFae().getYCoord() + STEP_SIZE);

            }
            /* if a hole is intersected by Fae */
            if (level0.holeIntersect()) {

                level0.getFae().setHealth(level0.getFae().getHealth() - Sinkhole.getDamagePoints());
                /* prints out player health to console */
                System.out.printf("Sinkhole inflicts %d damage points on Fae. Fae's current health: %d/%d\n",
                        Sinkhole.getDamagePoints(), level0.getFae().getHealth(), level0.getFae().MAX_HEALTH);

                /* changes health percentage colour */
                if (level0.getFae().getHealthPercentage() < LOW_HEALTH) {
                    /* change colour to red when health is low */
                    healthColour = RED;

                } else if (level0.getFae().getHealthPercentage() < MEDIUM_HEALTH) {
                    /* change colour to orange when medium health */
                    healthColour = ORANGE;
                }

            }
            /* sets the health bar colour */
            healthOptions.setBlendColour(healthColour);

            /* renders player, obstacles and background */
            BACKGROUND_IMAGE.draw(Window.getWidth() / 2.0, Window.getHeight() / 2.0);
            HEALTH_FONT.drawString(String.format("%d%%", level0.getFae().getHealthPercentage()), HEALTH_X, HEALTH_Y,
                    healthOptions);

            level0.getFae().drawPlayer();
            level0.drawSinkholes();
            level0.drawWalls();
        }

    }
}
