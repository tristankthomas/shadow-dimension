public class Demon extends Character {

    private static final double SPEED_FACTOR = 0.5;
    private static final int TIMESCALE_LOWER = -3;
    private static final int TIMESCALE_UPPER = 3;
    private final static int HEALTH_FONT_SIZE = 15;
    private static final int HEALTH_Y_OFFSET = 6;
    private static final State INITIAL_STATE = State.ATTACK;

    public Demon(int xCoord, int yCoord) {
       this.xCoord = xCoord;
       this.yCoord = yCoord;
       bar = new HealthBar(xCoord, yCoord - HEALTH_Y_OFFSET, HEALTH_FONT_SIZE);
       state = INITIAL_STATE;
    }
//            - healthX: int
//- healthY: int

}
