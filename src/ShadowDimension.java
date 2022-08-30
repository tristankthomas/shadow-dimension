import bagel.*;
import java.text.DecimalFormat;
import java.util.Scanner;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
/**
 * Skeleton Code for SWEN20003 Project 1, Semester 2, 2022
 *
 * Please enter your name below
 * @author Tristan Thomas
 */

public class ShadowDimension extends AbstractGame {
    private final static int WINDOW_WIDTH = 1024;
    private final static int WINDOW_HEIGHT = 768;
    private final static String GAME_TITLE = "SHADOW DIMENSION";
    private final Image BACKGROUND_IMAGE = new Image("res/background0.png");
    private final Font DEFAULT_FONT = new Font("res/frostbite.ttf", 75);
    private final Font INSTRUCTION_FONT = new Font("res/frostbite.ttf", 40);
    private boolean gameStart = false;

    public ShadowDimension(){
        super(WINDOW_WIDTH, WINDOW_HEIGHT, GAME_TITLE);
    }

    /**
     * The entry point for the program.
     */
    public static void main(String[] args) {
        ShadowDimension game = new ShadowDimension();
        game.run();
    }

    /**
     * Method used to read file and create objects (You can change this
     * method as you wish).
     */
    private void readCSV(){


    }

    /**
     * Performs a state update.
     * allows the game to exit when the escape key is pressed.
     */
    @Override
    protected void update(Input input) {


        if (input.wasPressed(Keys.ESCAPE))
            Window.close();
        else if (!gameStart) {
            if (input.wasPressed(Keys.SPACE))
                gameStart = true;
            DEFAULT_FONT.drawString(GAME_TITLE, 260, 250);
            INSTRUCTION_FONT.drawString("PRESS SPACE TO START", 350, 440);
            INSTRUCTION_FONT.drawString("USE ARROW KEYS TO FIND GATE", 350, 490);
        } else {
            BACKGROUND_IMAGE.draw(Window.getWidth() / 2.0, Window.getHeight() / 2.0);

        }

    }
}
