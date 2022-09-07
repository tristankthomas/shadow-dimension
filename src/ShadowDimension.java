import bagel.*;
import bagel.util.Colour;
import bagel.util.Point;

import java.io.FileReader;
import java.io.BufferedReader;
import java.util.ArrayList;
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
    private final static String WIN_MESSAGE = "CONGRATULATIONS!";
    private final static String LOSE_MESSAGE = "GAME OVER!";
    private String healthMessage = "";
    private DrawOptions healthOptions = new DrawOptions();
    private Colour healthColour = new Colour(0,0.8,0.2);
    private final Image BACKGROUND_IMAGE = new Image("res/background0.png");
    private final Font DEFAULT_FONT = new Font("res/frostbite.ttf", 75);
    private final Font INSTRUCTION_FONT = new Font("res/frostbite.ttf", 40);
    private final Font HEALTH_FONT = new Font("res/frostbite.ttf", 30);
    private final static int WALL_INTERSECT_OFFSET = 3;
    private boolean gameStart = false;

    // game entities
    private final static int MAX_OBJECTS = 60;
    private Player fae;
    private ArrayList<Wall> walls = new ArrayList<Wall>();
    private ArrayList<Sinkhole> sinkholes = new ArrayList<Sinkhole>();
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

    public Point getCentredCoord(String text) {
        return new Point(Window.getWidth() / 2 - DEFAULT_FONT.getWidth(text) / 2, Window.getHeight() / 2 + 75.0/2);
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
                        walls.add(new Wall(new Point(xCoord, yCoord)));
                        numWalls++;
                        break;
                    case "Sinkhole":
                        sinkholes.add(new Sinkhole(new Point(xCoord, yCoord)));
                        numSinkholes++;
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
        for (int i = 0; i < numSinkholes; i++) {
            sinkholes.get(i).drawSinkhole();
        }
    }

    // Draws all the walls stored in array
    private void drawWalls() {
        for (int i = 0; i < numWalls; i++) {
            walls.get(i).drawWall();
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

    // Finds the edge that is intersected with the wall
    public String pointCheck(Wall wall) {
        Point topLeft = fae.getRect().topLeft();
        Point botRight = fae.getRect().bottomRight();
        if (wall.getRect().intersects(new Point(fae.getRect().right(), topLeft.y + WALL_INTERSECT_OFFSET)) ||
                wall.getRect().intersects(new Point(fae.getRect().right(), botRight.y - WALL_INTERSECT_OFFSET)))
            return "right";
        else if (wall.getRect().intersects(new Point(fae.getRect().left(), topLeft.y + WALL_INTERSECT_OFFSET)) ||
                wall.getRect().intersects(new Point(fae.getRect().left(), botRight.y - WALL_INTERSECT_OFFSET)))
            return "left";
        else if (wall.getRect().intersects(new Point(topLeft.x + WALL_INTERSECT_OFFSET, fae.getRect().top())) ||
                wall.getRect().intersects(new Point(botRight.x - WALL_INTERSECT_OFFSET, fae.getRect().top())))
            return "up";
        else if (wall.getRect().intersects(new Point(topLeft.x + WALL_INTERSECT_OFFSET, fae.getRect().bottom())) ||
                wall.getRect().intersects(new Point(botRight.x - WALL_INTERSECT_OFFSET, fae.getRect().bottom())))
            return "down";

        return "";
    }

    // move into wall class
    public boolean wallIntersect(String direction) {
        boolean inter = false;
        for (Wall wall : walls) {
            if (wall != null && wall.getRect().intersects(fae.getRect())) {
                if (pointCheck(wall).equals(direction))
                    inter = true;
            }
        }
        return inter;
    }


    public boolean holeIntersect() {
        for (int i = 0; i < numSinkholes; i++) {
            if (sinkholes.get(i).getRect().intersects(fae.getRect())) {
                sinkholes.remove(i);
                numSinkholes--;
                return true;
            }
        }
        return false;
    }


    /**
     * Performs a state update.
     * allows the game to exit when the escape key is pressed.
     */
    @Override
    protected void update(Input input) {
        Point prev;

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

        } else if (fae.getXCoord() >= 950 && fae.getYCoord() >= 670) {
            Point textCoord = getCentredCoord(WIN_MESSAGE);
            DEFAULT_FONT.drawString(WIN_MESSAGE, textCoord.x, textCoord.y);
        } else if (fae.getHealth() <= 0) {
            Point textCoord = getCentredCoord(LOSE_MESSAGE);
            DEFAULT_FONT.drawString(LOSE_MESSAGE, textCoord.x, textCoord.y);
        } else {
            // moves player if not at boundary and arrow keys are pressed
            if (input.isDown(Keys.LEFT) && !atBoundary("left") && !wallIntersect("left")) {
                fae.setXCoord(fae.getXCoord() - STEP_SIZE);
                fae.setIsRight(false);
            }
            if (input.isDown(Keys.RIGHT) && !atBoundary("right") && !wallIntersect("right")) {
                fae.setXCoord(fae.getXCoord() + STEP_SIZE);
                fae.setIsRight(true);
            }
            if (input.isDown(Keys.UP) && !atBoundary("up") && !wallIntersect("up")) {
                fae.setYCoord(fae.getYCoord() - STEP_SIZE);
            }
            if (input.isDown(Keys.DOWN) && !atBoundary("down") && !wallIntersect("down")) {
                fae.setYCoord(fae.getYCoord() + STEP_SIZE);
            }
            if (holeIntersect()) {
                fae.setHealth(fae.getHealth() - Sinkhole.DAMAGE_POINTS);
                System.out.printf("Sinkhole inflicts %d damage points on Fae. Fae's current health: %d/%d\n", Sinkhole.DAMAGE_POINTS, fae.getHealth(), fae.MAX_HEALTH);
                if (fae.getHealthPercentage() < 35) {
                    healthColour = new Colour(1,0,0);
                } else if (fae.getHealthPercentage() < 65) {
                    healthColour = new Colour(0.9,0.6,0);
                }

            }
            healthOptions.setBlendColour(healthColour);
            // renders player, obstacles and background
            BACKGROUND_IMAGE.draw(Window.getWidth() / 2.0, Window.getHeight() / 2.0);
            HEALTH_FONT.drawString(String.format("%d%%", fae.getHealthPercentage()), 20, 25, healthOptions);
            fae.drawPlayer();
            drawSinkholes();
            drawWalls();
        }

    }
}
