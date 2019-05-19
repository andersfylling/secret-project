package team.adderall.game;


import addy.annotations.*;

@Service("GameState")
public class GameState
{
    /**
     * Fixed screen size. This is the size used for calculations
     */
    public static final int FIXED_HEIGHT = 1000;
    public static final int FIXED_WIDTH = 1500;
    public static final int FIXED_BALL_RADIUS = 25;
    public static final int FIXED_AID_LENGTH = 50;
    public static final int FIXED_AID_HEIGHT = 2*FIXED_AID_LENGTH;
    public static final int FIXED_THICKNESS = 50;
    public static final int FIXED_JUMP = -8;


    private float yScrollValue=0;
    private float xScale = 0;
    private float yScale = 0;


    @DepWire
    public GameState()
    {}

    /**
     * Set Y value for how much the layers window have scrolled
     * @param y
     */
    public void setYScrollValue(float y){
        this.yScrollValue = y;
    }

    /**
     * Get Y value for how much the layers window have scrolled
     */
    public float getYScrollValue(){
        return yScrollValue;
    }


    /**
     * Get the scale for x direction
     * @return float scale
     */
    public float getXScale() {
        return xScale;
    }

    /**
     * Get the scale for y direction
     * @return float scale
     */
    public float getYScale() {
        return yScale;
    }

    /**
     * set the scale for y direction
     * @return void
     */
    public void setYScale(float y){
        this.yScale = y;
    }

    /**
     * Get the scale for x direction
     * @return void
     */
    public void setXScale(float x){
        this.xScale = x;
    }
}
