package team.adderall.game.framework;

import android.app.Activity;
import android.graphics.Canvas;
import android.view.View;

import java.util.ArrayList;

import team.adderall.game.GameState;
import team.adderall.game.framework.component.GameComponent;
import team.adderall.game.framework.component.GameDepWire;
import team.adderall.game.framework.component.Inject;
import team.adderall.game.framework.context.GameContext;

@GameComponent("GraphicsManager")
public class GraphicsManager
        extends
        View
        implements
        GamePainter
{
    private static final double SCROLLSPEED = 0.1;
    private final Activity activity;
    private final GamePainter[][] painters;
    private final GameState gameState;
    private ArrayList<GamePainter[]> gameObjects;
    private ArrayList<GamePainter[]> fixedPositionObjects;
    private long lastRun =0;


    /**
     * Simple constructor to use when creating a view from code.
     *
     * @param activity Get the Context the view is running in, through which it can
     *                access the current theme, resources, etc.
     */
    @GameDepWire
    public GraphicsManager(@Inject("activity") final Activity activity,
                           @Inject(GameContext.PAINT) GamePainter[][] painters,
                           @Inject("GameState") GameState gameState)
    {
        super(activity);

        this.activity = activity;
        this.painters = painters;
        this.gameState = gameState;

        this.fixedPositionObjects = new ArrayList<>();
        this.gameObjects = new ArrayList<>();

        this.lastRun= System.nanoTime();

        /**
         * First element of the array is stationary objects
         * The rest are moving parts of the game.
         */
        int i = 0;
        for (GamePainter[] layerPainters : this.painters) {
            if(i==0){
                fixedPositionObjects.add(layerPainters);
            }
            else{
                gameObjects.add(layerPainters);
            }
            i++;
        }
        // bind this view to the game activity for rendering/painting/drawing
        final GraphicsManager self = this;
        this.activity.runOnUiThread(() -> self.activity.setContentView(self));
    }



    public void redraw()
    {
        final GraphicsManager self = this;
        this.activity.runOnUiThread(self::invalidate);
    }

    @Override
    protected void onDraw(final Canvas canvas)
    {
        super.onDraw(canvas);
        this.paint(canvas);
        this.paint(canvas,this.getScrollY());

        this.updateScrollY();
    }

    /**
     * Updates the Scroll Value
     * And saves the updated value in gameState
     */
    private void updateScrollY()
    {
        setNewScrollValue();
        this.gameState.setyScaleValue(this.getScrollY());
    }

    @Override
    /**
     * Draw objects that are a part of the game
     */
    public void paint(final Canvas canvas)
    {
        for (GamePainter[] layerPainters : this.gameObjects) {
            for (GamePainter painter : layerPainters) {
                painter.paint(canvas);
            }
        }

    }

    @Override
    /**
     * Draw Objects that are fixed
     */
    public void paint(Canvas canvas, float y)
    {
        for (GamePainter[] layerPainters : this.fixedPositionObjects) {
            for (GamePainter painter : layerPainters) {
                painter.paint(canvas,y);
            }
        }
    }

    /**
     * Update the Scrolling of the Floors based on time since last update.
     * This way it should be quite similar when playing multiplayer.
     */
    public void setNewScrollValue()
    {
        long now = System.nanoTime();

        double diff = (now - this.lastRun) / 10000000.0;
        double update = diff * this.SCROLLSPEED;

        if(update>1) {
            this.lastRun = now;
            this.scrollBy(0, (int) -update);
        }
    }
}
