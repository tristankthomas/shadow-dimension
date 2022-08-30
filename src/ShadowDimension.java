import bagel.*;
import bagel.util.Point;

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
    // game specs
    private final static int WINDOW_WIDTH = 1024;
    private final static int WINDOW_HEIGHT = 768;
    private final static String GAME_TITLE = "SHADOW DIMENSION";
    private final Image BACKGROUND_IMAGE = new Image("res/background0.png");
    private final Font DEFAULT_FONT = new Font("res/frostbite.ttf", 75);
    private final Font INSTRUCTION_FONT = new Font("res/frostbite.ttf", 40);
    private boolean gameStart = false;

    // game entities
    private final static int MAX_OBJECTS = 60;
    private Player fae;
    private Wall[] walls = new Wall[MAX_OBJECTS];
    private Sinkhole[] sinkholes = new Sinkhole[MAX_OBJECTS];
    private int numSinkholes = 0;
    private int numWalls = 0;
    private Point topLeftBound;
    private Point botRightBound;
    private final static int STEP_SIZE = 2;

    public ShadowDimension() {
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
    private void readCSV() {
        int xCoord, yCoord;
        String type;

        try (BufferedReader br = new BufferedReader(new FileReader("res/level0.csv"))) {
            String line = null;
            // reads line by line until end of file
            while ((line = br.readLine()) != null) {

                // splits line into elements
                String[] data = line.split(",");

                type = data[0];
                xCoord = Integer.parseInt(data[1]);
                yCoord = Integer.parseInt(data[2]);

                // creates new objects based on data
                switch(type) {
                    case "Player":
                        fae = new Player(xCoord, yCoord);
                        break;
                    case "Wall":
                        walls[numWalls++] = new Wall(xCoord, yCoord);
                        break;
                    case "Sinkhole":
                        sinkholes[numSinkholes++] = new Sinkhole(xCoord, yCoord);
                        break;
                    case "TopLeft":
                        topLeftBound = new Point(xCoord, yCoord);
                    case "BottomRight":
                        botRightBound = new Point(xCoord, yCoord);
                    default:
                        break;
                }
           }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Draws all the sinkholes stored in array
    private void drawSinkholes() {
        for (Sinkhole sinkhole : sinkholes) {
            if (sinkhole != null) sinkhole.drawSinkhole();
        }
    }

    // Draws all the walls stored in array
    private void drawWalls() {
        for (Wall wall : walls) {
            if (wall != null) wall.drawWall();
        }
    }

    // Checks if player will hit the boundary
    private boolean atBoundary(String direction) {
        switch(direction) {
            case "left":
                return fae.getXCoord() <= topLeftBound.x;
            case "right":
                return fae.getXCoord() >= botRightBound.x;
            case "up":
                return fae.getYCoord() <= topLeftBound.y;
            case "down":
                return fae.getYCoord() >= botRightBound.y;
            default:
                return false;
        }

    }


    /**
     * Performs a state update.
     * allows the game to exit when the escape key is pressed.
     */
    @Override
    protected void update(Input input) {


        if (input.wasPressed(Keys.ESCAPE))
            // closes window
            Window.close();
        else if (!gameStart) {
            // waits for game to start
            if (input.wasPressed(Keys.SPACE)) {
                // game has started
                gameStart = true;
                readCSV();
            }
            // title screen
            DEFAULT_FONT.drawString(GAME_TITLE, 260, 250);
            INSTRUCTION_FONT.drawString("PRESS SPACE TO START", 350, 440);
            INSTRUCTION_FONT.drawString("USE ARROW KEYS TO FIND GATE", 350, 490);

        } else {
            // moves player if not at boundary and arrow keys are pressed
            if (input.isDown(Keys.LEFT) && !atBoundary("left")) {
                fae.setXCoord(fae.getXCoord() - STEP_SIZE);
                fae.setIsRight(false);
            }
            if (input.isDown(Keys.RIGHT) && !atBoundary("right")) {
                fae.setXCoord(fae.getXCoord() + STEP_SIZE);
                fae.setIsRight(true);
            }
            if (input.isDown(Keys.UP) && !atBoundary("up")) {
                fae.setYCoord(fae.getYCoord() - STEP_SIZE);
            }
            if (input.isDown(Keys.DOWN) && !atBoundary("down")) {
                fae.setYCoord(fae.getYCoord() + STEP_SIZE);
            }
            // renders player and background
            BACKGROUND_IMAGE.draw(Window.getWidth() / 2.0, Window.getHeight() / 2.0);
            fae.drawPlayer();
            drawSinkholes();
            drawWalls();
        }

    }
}
