package team.adderall.game.ball;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;


import team.adderall.game.PositionTracker;
import team.adderall.game.userinput.SensorEvt;
import team.adderall.game.userinput.SensorEvtListener;
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
        implements
        SensorEvtListener
{
    // defaults
    // |
    // +- movement speed / sensitivity
    private final static double SPEED = 1.75;
    // |
    // +- ball radius
    private final static int RADIUS = 45;
    // |
    // +- movement threshold
    private final static int MOVEMENT_THRESHOLD = 0;
    // |
    // +- dead or alive state
    public final static int STATE_ALIVE = 1;
    public final static int STATE_DEAD = 2;


    // behavior
    private double speed;

    private double velocity;
    private double jumpForce;

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
        tracker.setposition(new Point(100, 0));

        this.painter = new Paint();
        this.painter.setColor(Color.parseColor(ball.getColour()));
        this.painter.setStyle(Paint.Style.FILL);

        this.deathPainter = new Paint();
        this.deathPainter.setColor(Color.RED);
        this.deathPainter.setTextSize(75);
        this.deathPainter.setTextAlign(Paint.Align.CENTER);

        this.speed = SPEED;
        this.state = STATE_ALIVE;
        this.activePlayer = true;

        this.velocity = 0;
        jumpForce = 0;
    }

    /**
     * Constructor
     */
    public BallManager(final boolean activePlayer) {

        this.ball = new Ball(RADIUS);

        this.tracker = new PositionTracker(MOVEMENT_THRESHOLD);
        tracker.setposition(new Point(100, 0));

        this.painter = new Paint();
        this.painter.setColor(Color.parseColor(ball.getColour()));
        this.painter.setStyle(Paint.Style.FILL);

        this.deathPainter = new Paint();
        this.deathPainter.setColor(Color.RED);
        this.deathPainter.setTextSize(75);
        this.deathPainter.setTextAlign(Paint.Align.CENTER);

        this.speed = SPEED;
        this.state = STATE_ALIVE;
        this.activePlayer = activePlayer;

        this.velocity = 0;
    }



    public void setSpeed(double speed) {
        this.speed = speed;
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
     */
    public void paint(Canvas canvas) {
        // draw the ball
        canvas.drawCircle(this.tracker.getX(), this.tracker.getY(), this.ball.getRadius(), this.painter);
        // update the tracker
        this.tracker.updateOldPosition();

    }

    /**
     * Listen for tilt and what not
     */
    @Override
    public void onSensorEvt(SensorEvt evt) {
        if (this.state == STATE_DEAD) {
            return;
        }

        int xChange = (int) ((-evt.getX()) * this.speed);
        int yChange = (int) ((evt.getY()) * this.speed);


        // update ball position
        // yDiff is 0 as we currently are only moving along one axis.
        this.tracker.addToPosition(xChange, 0);

    }

    //public void setDeathZone(DeathZone deathZone) {
    //    this.deathZone = deathZone;
    //}

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Point getPos() {
        Point point = new Point(this.tracker.getX(),this.tracker.getY());
        return point;
    }
    public void setPos(Point point){
        this.tracker.setposition(point);
    }

    public void colided(Rect squere) {
        this.tracker.colided(squere);

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

    public void drawHighScore(Canvas canvas, float y) {
        this.tracker.drawHighScore(canvas,y);
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

    public double getJumpForce() {
        return jumpForce;
    }

    public void setJumpForce(double jumpForce) {
        this.jumpForce = jumpForce;
    }
}