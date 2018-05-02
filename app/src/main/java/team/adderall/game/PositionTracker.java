package team.adderall.game;

import android.graphics.Canvas;
import android.graphics.Rect;

import team.adderall.game.highscore.HighScore;


public class PositionTracker
{
    private static final double SPEEDREDUCTION = 0.2;
    private final int movementThreshold;
    private HighScore highScore = null;
    private boolean blocked;
    private double x;
    private double y;
    private double verticalSpeed = 0;
    // used to verify if the object has moved
    private double oldX;
    private double oldY;

    // Not constant, as we might want to use it for something cool later on.
    private double gravity = 9.8;
    private double terminalVel = 10;

    private long oldTime;
    private boolean colide = false;
    private double newx;
    private double newy;



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

    public void addToPosition(double xDiff, double yDiff) {
        this.x += xDiff;
        this.y += yDiff; // y is reversed
    }

    public void updatePlayerPos(){
        this.y = this.y;
        this.x = this.x;
    }

    public void updatePosition(double x, double y) {
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

        double xDiff = this.oldX - this.x;
        double yDiff = this.oldY - this.y;

        if (this.movementThreshold == 0) {
            return xDiff != 0 || yDiff != 0;
        } else {
            // improve performance & stability by reducing accuracy
            boolean xPosChanged = xDiff > this.movementThreshold || xDiff < -this.movementThreshold;
            boolean yPostChanged = yDiff > this.movementThreshold || yDiff < -this.movementThreshold;
            return xPosChanged || yPostChanged;
        }
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getOldX() {
        return this.oldX;
    }

    public double getOldY() {
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
    public void addVelocity(double newVelocity) {
        this.verticalSpeed = newVelocity;
    }

    public Rect getRect()
    {
        final int margin = 45;

        int left    = (int) this.getX() - margin;
        int top     = (int) this.getY() - margin;
        int right   = (int) this.getX() + margin;
        int bottom  = (int) this.getY() + margin;

        return new Rect(left, top, right, bottom);
    }

    public void setposition(double x, double y) {
        this.x = x;
        this.y = y;
        highScore.potensiallySetHighestXValue(y);
    }

    public void drawHighScore(Canvas canvas, float y, String name, int playerNumber) {
        if(this.highScore != null) {
            this.highScore.paint(canvas, y, name, playerNumber);
        }
    }

    public void updateAidExtraScore(int i) {
        if(this.highScore != null) {
            this.highScore.updateAidExtraScore(i);
        }
    }

    public int getScore() {
        return highScore == null ? 0 : highScore.getScaledHighScore();
    }

    public void setY(double y) {
        this.y = y;
    }
}
