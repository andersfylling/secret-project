package team.adderall.game.ball;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;


import team.adderall.game.GameState;
import team.adderall.game.PositionTracker;
import team.adderall.game.framework.component.GameComponent;
import team.adderall.game.framework.component.GameDepWire;

/**
 * Handles rendering of the ball.
 *
 * TODO: how do we deal with collision detection? perhaps use a class to bind BallManager
 * TODO: and LevelManager somehow.. beans + DI?
 */
@GameComponent
public class BallManager
{
    // defaults
    // |
    // +- ball radius
    private final static int RADIUS = GameState.FIXED_BALL_RADIUS;
    // |
    // +- movement threshold
    private final static int MOVEMENT_THRESHOLD = 0;
    // |
    // +- dead or alive state
    public final static int STATE_ALIVE = 1;
    public final static int STATE_DEAD = 2;


    // behavior
    private double velocity;

    // Ball details
    private Ball ball;

    // track ball position
    private PositionTracker tracker;

    // details for paining the ball
    private Paint painter;
    private Paint deathPainter;
    //private DeathZone deathZone;
    private int state;
    private final boolean activePlayer;


    private boolean atGround = false;

    /**
     * Constructor
     * Default player
     */
    @GameDepWire
    public BallManager() {

        this.ball = new Ball(RADIUS);

        this.tracker = new PositionTracker(MOVEMENT_THRESHOLD);
        tracker.setposition(100, 0);

        this.painter = new Paint();
        this.painter.setColor(Color.parseColor(ball.getColour()));
        this.painter.setStyle(Paint.Style.FILL);

        this.deathPainter = new Paint();
        this.deathPainter.setColor(Color.RED);
        this.deathPainter.setTextSize(75);
        this.deathPainter.setTextAlign(Paint.Align.CENTER);

        this.state = STATE_ALIVE;
        this.activePlayer = true;

        this.velocity = 0;
    }

    /**
     * Constructor
     */
    public BallManager(final boolean activePlayer) {

        this.ball = new Ball(RADIUS);

        this.tracker = new PositionTracker(MOVEMENT_THRESHOLD);
        tracker.setposition(100, 0);

        this.painter = new Paint();
        this.painter.setColor(Color.parseColor(ball.getColour()));
        this.painter.setStyle(Paint.Style.FILL);

        this.deathPainter = new Paint();
        this.deathPainter.setColor(Color.RED);
        this.deathPainter.setTextSize(75);
        this.deathPainter.setTextAlign(Paint.Align.CENTER);

        this.state = STATE_ALIVE;
        this.activePlayer = activePlayer;

        this.velocity = 0;
    }

    public void moveBallTo(int x, int y) {
        this.tracker.updatePosition(x, y);
    }

    /**
     * Move the ball a little without knowing the end position.
     *
     * @deprecated Let the ball manager deal with ball movements on it's own
     * @param xDiff
     * @param yDiff
     */
    public void moveBall(int xDiff, int yDiff) {

        this.tracker.addToPosition(xDiff, yDiff);
    }

    /**
     * Paint / draw the ball
     * @param canvas
     * @param scale
     */
    public void paint(Canvas canvas, float scale) {
        // draw the ball
        canvas.drawCircle((int) tracker.getX() * scale, (int) tracker.getY(), this.ball.getRadius() * scale, this.painter);
        // update the tracker
        this.tracker.updateOldPosition();

    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public double getX() {
        return tracker.getX();
    }
    public double getY() {
        return tracker.getY();
    }
    public void setY(double y) {
        tracker.setY(y);
    }
    public void addToY(double y)
    {
    }

    public void addToX(double x)
    {
        tracker.addToPosition(x, 0);
    }

    public void setPos(double x, double y) {
        tracker.setposition(x, y);
    }

    public void doGravity() {
        this.tracker.fallToTheGround();
    }
    public void updatePlayerPos(){
        this.tracker.updatePlayerPos();
    }


    public double getVelocity() {
        return velocity;
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    public boolean isActivePlayer() {
        return activePlayer;
    }

    public Ball getBall() {
        return ball;
    }

    public void drawHighScore(Canvas canvas, float y, String name, int playerNumber) {
        this.tracker.drawHighScore(canvas,y,name,playerNumber);
    }

    public void setAtGround(boolean atGround) {
        this.atGround = atGround;
    }
    public boolean getAtGround(){
        return this.atGround;
    }
    public void updateAidExtraScore(int i) {
        this.tracker.updateAidExtraScore(i);
    }

    public int getScore() {
        return this.tracker.getScore();
    }
}