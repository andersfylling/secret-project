package team.adderall.game;


import team.adderall.game.framework.component.GameComponent;
import team.adderall.game.framework.component.GameDepWire;

@GameComponent("GameState")
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
    private float xscale = 0;
    private float yscale = 0;


    @GameDepWire
    public GameState() {
    }

    /**
     * Set Y value for how much the layers window have scrolled
     * @param y
     */
    public void setyScrollValue(float y){
        this.yScrollValue = y;
    }

    /**
     * Get Y value for how much the layers window have scrolled
     */
    public float getyScrollValue(){
        return yScrollValue;
    }


    /**
     * Get the scale for x direction
     * @return float scale
     */
    public float getxScale() {
        return xscale;
    }

    /**
     * Get the scale for y direction
     * @return float scale
     */
    public float getyScale() {
        return yscale;
    }

    /**
     * set the scale for y direction
     * @return void
     */
    public void setyScale(float y){
        this.yscale = y;
    }

    /**
     * Get the scale for x direction
     * @return void
     */
    public void setxScale(float x){
        this.xscale = x;
    }

}
