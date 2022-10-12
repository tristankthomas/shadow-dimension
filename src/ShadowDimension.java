import bagel.*;
import bagel.util.Point;

/**
 * SWEN20003 Project 2, Semester 2, 2022
 *
 * ShadowDimension.java: Main Shadow Dimension class used to run game
 * Certain object-oriented aspects of this game were inspired from the Project 1 sample solution
 *
 * @author Tristan Thomas
 */
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
    /* coordinates */
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


    /**
     * Constructor for ShadowDimension class
     */
    public ShadowDimension() {
        super(WINDOW_WIDTH, WINDOW_HEIGHT, GAME_TITLE);
    }


    /**
     * Entry point for the program
     * @param args
     *
     */
    public static void main(String[] args) {
        ShadowDimension game = new ShadowDimension();
        game.run();
    }



    /**
     * Calculates the centre coordinates for a given string (top left of string)
     * @param text
     * @return
     */
    public Point getCentredCoord(String text) {
        return new Point(Window.getWidth() / 2 - DEFAULT_FONT.getWidth(text) / 2,
                Window.getHeight() / 2 + (double) DEFAULT_FONT_SIZE / 2);
    }


    /**
     * Performs a state update at the refresh rate of screen
     * @param input
     */
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
            /* waits for game to start (after the level complete screen is finished) */
            if (input.wasPressed(Keys.SPACE)) {
                /* game has started */
                level1Start = true;
                /* creates a new world layout (level 0) */
                level1 = new World(SECOND_WORLD_FILE);
            }
            /* level 1 screen */
            INSTRUCTION_FONT.drawString(INSTRUCTION2, INSTRUCTION2_X, INSTRUCTION2_Y);

        } else if ((level0.getFae().getXCoord() >= PORTAL_X && level0.getFae().getYCoord() >= PORTAL_Y && level0Start) ||
                (input.wasPressed(Keys.W) && level0Start) || levelFrameCount != 0) {
            /* level win screen (can be entered by pressing W as well as entering portal) */
            Point levelWinTextCoord = getCentredCoord(WIN_LEVEL_MESSAGE);
            /* counts the number of frames (for 3 seconds) */
            levelFrameCount = (levelFrameCount / Character.getFramesPerMs() != LEVEL_COMPLETE_TIME_MS) ? levelFrameCount + 1 : 0;
            DEFAULT_FONT.drawString(WIN_LEVEL_MESSAGE, levelWinTextCoord.x, levelWinTextCoord.y);
            level0Start = false;
            level0Finish = true;

        } else if (level0.getFae().isDead()) {

            /* lose screen */
            Point loseTextCoord = getCentredCoord(LOSE_MESSAGE);
            DEFAULT_FONT.drawString(LOSE_MESSAGE, loseTextCoord.x, loseTextCoord.y);

        } else if (level0Start) {

            BACKGROUND_IMAGE0.draw(Window.getWidth() / 2.0, Window.getHeight() / 2.0);

            /* moves player based on input */
            level0.getFae().move(input, level0);

            /* checks if sinkhole is hit */
            level0.holeIntersect(level0.getFae());

            /* attack mode entered */
            if ((input.wasPressed(Keys.A) && !level0.getFae().getIsCoolDown()) || level0.getFae().getIsAttack()) {
                level0.getFae().attack(level0, 0);

            }

            /* cooldown mode entered (after attack finished) */
            if (level0.getFae().getIsCoolDown()) {
                level0.getFae().cooldown();
            }

            /* renders obstacles and fae */
            level0.drawObstacles();
            level0.getFae().drawCharacter();


        } else if (level1.getNavec().isDead()) {
            /* win screen once navec is killed */
            Point winTextCoord = getCentredCoord(WIN_MESSAGE);
            DEFAULT_FONT.drawString(WIN_MESSAGE, winTextCoord.x, winTextCoord.y);

        }

        else if (level1Start) {

            BACKGROUND_IMAGE1.draw(Window.getWidth() / 2.0, Window.getHeight() / 2.0);

            /* moves player if not at a boundary and arrow keys are pressed */
            level1.getFae().move(input, level1);

            /* attack mode entered */
            if ((input.wasPressed(Keys.A) && !level1.getFae().getIsCoolDown()) || level1.getFae().getIsAttack()) {
                level1.getFae().attack(level1, 1);

            }

            /* cooldown mode entered (after attack finished) */
            if (level1.getFae().getIsCoolDown()) {
                level1.getFae().cooldown();
            }

            /* increases speed of demons */
            if (input.wasPressed(Keys.L)) {
                level1.increaseMovement();
            }
            /* decreases speed of demons */
            if (input.wasPressed(Keys.K)) {
                level1.decreaseMovement();
            }

            /* updates all character invincibility */
            level1.charactersInvincible();
            /* updates all demons movement */
            level1.demonsMove();
            /* checks if fae intersects fire */
            level1.demonProximityCheck();
            /* if a hole is intersected by Fae */
            level1.holeIntersect(level1.getFae());


            /* renders fae, demons and obstacles */
            level1.drawObstacles();
            level1.drawDemons();
            level1.getFae().drawCharacter();



        }

    }
}
