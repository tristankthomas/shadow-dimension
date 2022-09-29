/**
 * SWEN20003 Project 2, Semester 2, 2022
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
    private final static String WIN_LEVEL_MESSAGE = "LEVEL COMPLETE!";
    private final static String WIN_MESSAGE = "CONGRATULATIONS!";
    private final static String LOSE_MESSAGE = "GAME OVER!";
    private final static String INSTRUCTION1 = "PRESS SPACE TO START\nUSE ARROW KEYS TO FIND GATE";
    private final static String INSTRUCTION2 = "PRESS SPACE TO START\nPRESS A TO ATTACK\nDEFEAT NAVEC TO WIN";
    private final String FIRST_WORLD_FILE = "res/level0.csv";
    private final String SECOND_WORLD_FILE = "res/level1.csv";
    private final Image BACKGROUND_IMAGE0 = new Image("res/background0.png");
    private final Image BACKGROUND_IMAGE1 = new Image("res/background1.png");

    /* font definitions */
    private final static int DEFAULT_FONT_SIZE = 75;
    private final static int INSTRUCTION_FONT_SIZE = 40;

    private final Font DEFAULT_FONT = new Font("res/frostbite.ttf", DEFAULT_FONT_SIZE);
    private final Font INSTRUCTION_FONT = new Font("res/frostbite.ttf", INSTRUCTION_FONT_SIZE);
    /* text coordinates */
    private final int PORTAL_X = 950;
    private final int PORTAL_Y = 670;
    private final int TITLE_X = 260;
    private final int TITLE_Y = 250;
    private final int INSTRUCTION1_X = 350;
    private final int INSTRUCTION1_Y = 440;
    private final int INSTRUCTION2_X = 350;
    private final int INSTRUCTION2_Y = 350;

    private int levelFrameCount = 0;
    private static final int LEVEL_COMPLETE_TIME_MS = 3000;

    /* World object representing level 0 of Shadow Dimension */
    private World level0;
    private World level1;
    private boolean level0Start = false;
    private boolean level0Finish = false;
    private boolean level1Start = false;


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
        else if (!level0Start && !level0Finish) {
            /* waits for game to start */
            if (input.wasPressed(Keys.SPACE)) {
                /* game has started */
                level0Start = true;
                /* creates a new world layout (level 0) */
                level0 = new World(FIRST_WORLD_FILE);
            }
            /* title screen */
            DEFAULT_FONT.drawString(GAME_TITLE, TITLE_X, TITLE_Y);
            INSTRUCTION_FONT.drawString(INSTRUCTION1, INSTRUCTION1_X, INSTRUCTION1_Y);

        } else if (!level1Start && level0Finish && levelFrameCount == 0) {
            /* waits for game to start */
            if (input.wasPressed(Keys.SPACE)) {
                /* game has started */
                level1Start = true;
                /* creates a new world layout (level 0) */
                level1 = new World(SECOND_WORLD_FILE);
            }
            /* level 1 screen */
            INSTRUCTION_FONT.drawString(INSTRUCTION2, INSTRUCTION2_X, INSTRUCTION2_Y);

        } else if ((level0.getFae().getXCoord() >= PORTAL_X && level0.getFae().getYCoord() >= PORTAL_Y && level0Start) || (input.wasPressed(Keys.W) && level0Start) || levelFrameCount != 0) {
            /* level win screen */
            Point levelWinTextCoord = getCentredCoord(WIN_LEVEL_MESSAGE);
            /* counts the number of frames (for 3 seconds) */
            levelFrameCount = (levelFrameCount / Character.getFramesPerMs() != LEVEL_COMPLETE_TIME_MS) ? levelFrameCount + 1 : 0;
            DEFAULT_FONT.drawString(WIN_LEVEL_MESSAGE, levelWinTextCoord.x, levelWinTextCoord.y);
            level0Start = false;
            level0Finish = true;

        }else if (World.getFae().isDead()) {

            /* lose screen */
            Point loseTextCoord = getCentredCoord(LOSE_MESSAGE);
            DEFAULT_FONT.drawString(LOSE_MESSAGE, loseTextCoord.x, loseTextCoord.y);

        } else if (level0Start) {
            BACKGROUND_IMAGE0.draw(Window.getWidth() / 2.0, Window.getHeight() / 2.0);
            /* moves player if not at a boundary and arrow keys are pressed */
            World.getFae().move(input, level0);

            /* if a hole is intersected by Fae */
            if (level0.holeIntersect(World.getFae())) {
                // ADD THIS CODE TO A DIFFERENT CLASS PERHAPS SINKHOLE CLASS
                World.getFae().setHealth(World.getFae().getHealth() - Sinkhole.getDamagePoints());
                /* prints out player health to console */
                System.out.printf("Sinkhole inflicts %d damage points on Fae. Fae's current health: %d/%d\n",
                        Sinkhole.getDamagePoints(), World.getFae().getHealth(), World.getFae().getMaxHealth());

            }

            /* renders player, obstacles and background */

            level0.drawObstacles();
            World.getFae().drawCharacter();


        } else if (level1.getNavec().isDead()) {
            /* win screen */
            Point winTextCoord = getCentredCoord(WIN_MESSAGE);
            DEFAULT_FONT.drawString(WIN_MESSAGE, winTextCoord.x, winTextCoord.y);

        }

        else if (level1Start) {

            BACKGROUND_IMAGE1.draw(Window.getWidth() / 2.0, Window.getHeight() / 2.0);
            /* moves player if not at a boundary and arrow keys are pressed */
            World.getFae().move(input, level1);

            if ((input.wasPressed(Keys.A) && !World.getFae().getIsCoolDown()) || World.getFae().getIsAttack()) {
                World.getFae().attack(level1);

            }
            if (input.wasPressed(Keys.L)) {
                Demon.increaseTimescale(level1);
            }
            if (input.wasPressed(Keys.K)) {
                Demon.decreaseTimescale(level1);
            }

            if (World.getFae().getIsCoolDown()) {
                World.getFae().cooldown();
            }

            level1.charactersInvincible();
            level1.demonsMove();
            level1.proximityCheck();



            /* if a hole is intersected by Fae */
            if (level1.holeIntersect(World.getFae())) {

                World.getFae().setHealth(World.getFae().getHealth() - Sinkhole.getDamagePoints());
                /* prints out player health to console */
                System.out.printf("Sinkhole inflicts %d damage points on Fae. Fae's current health: %d/%d\n",
                        Sinkhole.getDamagePoints(), World.getFae().getHealth(), World.getFae().getMaxHealth());
            }

            /* renders player, obstacles and background */

            level1.drawObstacles();
            level1.drawDemons();
            World.getFae().drawCharacter();



        }

    }
}
