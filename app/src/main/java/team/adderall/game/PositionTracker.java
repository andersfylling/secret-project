package team.adderall.game;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;

import team.adderall.game.highscore.HighScore;

/**
 * Created by Cim on 14/4/18.
 */

public class PositionTracker
{
    private static final double SPEEDREDUCTION = 0.2;
    private final int movementThreshold;
    private HighScore highScore = null;
    private boolean blocked;
    private int x;
    private int y;
    private double verticalSpeed = 0;
    // used to verify if the object has moved
    private int oldX;
    private int oldY;

    // Not constant, as we might want to use it for something cool later on.
    double gravity = 9.8;
    private int terminalVel = 10;

    private long oldTime;
    private boolean colide = false;
    private int newx;
    private int newy;



    /**
     * Constructor
     *
     * @param movementThreshold Threshold decides how many pixels the ball must move before it's
     *                          considered a reasonable change to (say) repaint the ball.
     */
    public PositionTracker(final int movementThreshold) {
        this.movementThreshold = movementThreshold;
        this.reset();
    }

    private void reset() {
        this.blocked = false;
        this.highScore  = new HighScore();

        this.x = 0;
        this.y = 0;
        this.newx = 0;
        this.newy = 0;
        this.oldX = this.x;
        this.oldY = this.y;
    }

    public void addToPosition(int xDiff, int yDiff) {
        this.x += xDiff;
        this.y += yDiff; // y is reversed
    }

    /**
     * Gravity implemetation.
     * Fall to the ground at a constant rate.
     */
    public void fallToTheGround() {
        this.verticalSpeed += gravity *0.5* partOfSecond(System.currentTimeMillis());
        if(this.verticalSpeed > terminalVel){
            this.verticalSpeed = terminalVel;
        }
        this.y += this.verticalSpeed;
    }
    public void updatePlayerPos(){
        this.y = this.y;
        this.x = this.x;
    }

    /**
     * Return how long it have passed since the last update, in a percentage of frames.
     * Given that we uses 60 frames per second
     * @param curtime
     * @return difference
     */
    private double partOfSecond(long curtime) {
        double diff = (curtime - this.oldTime);
        this.oldTime = curtime;
        return diff/1000*60;
    }

    public void updatePosition(int x, int y) {

        this.x = x;
        this.y = y;

    }

    public void updateOldPosition() {
        this.oldX = this.x;
        this.oldY = this.y;
    }

    public boolean moved()
    {
        if(this.colide){
            this.x = this.oldX;
            this.y = this.oldY;
        }

        int xDiff = this.oldX - this.x;
        int yDiff = this.oldY - this.y;

        if (this.movementThreshold == 0) {
            return xDiff != 0 || yDiff != 0;
        } else {
            // improve performance & stability by reducing accuracy
            boolean xPosChanged = xDiff > this.movementThreshold || xDiff < -this.movementThreshold;
            boolean yPostChanged = yDiff > this.movementThreshold || yDiff < -this.movementThreshold;
            return xPosChanged || yPostChanged;
        }
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getOldX() {
        return this.oldX;
    }

    public int getOldY() {
        return this.oldY;
    }

    public void setBlocked() {
        this.blocked = true;
    }

    public boolean getBlocked() {
        return this.blocked;
    }

    public void setUnblocked() {
        this.blocked = false;
    }

    /**
     * Add Velocity.
     * Currently sets it.
     * TODO: should it be +=, and should I have some limits. So a user cant chain jumps together?
     * @param newVelocity
     */
    public void addVelocity(int newVelocity) {
        this.verticalSpeed = newVelocity;
    }

    public void colided(Rect squere) {
        setPosThatDoesNotCrashWith(squere);

    }
    public Rect getRect(){
        Rect ball = new Rect(this.getX() -45, this.getY()-45, this.getX() + 45, this.getY()+45);
        return ball;
    }

    public void setPosThatDoesNotCrashWith(Rect squere) {
        //Collision c = new Collision();
        //Point newCord = c.getNoneIntercetCord(getRect(),squere);
        //setposition(newCord);
    }

    public void setposition(Point position) {
        this.x = position.x;
        this.y = position.y;
        highScore.potensiallySetHighestXValue(y);
    }

    public void drawHighScore(Canvas canvas, float y) {
        if(this.highScore != null)
            this.highScore.paint(canvas, y);
    }
}
