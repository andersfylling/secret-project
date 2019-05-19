package team.adderall.game.gameloop;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Canvas;
import android.view.View;

import java.util.ArrayList;

import addy.annotations.*;
import team.adderall.game.GameState;

@SuppressLint("ViewConstructor")
@Service("GraphicsManager")
public class GraphicsManager
        extends
        View
        implements
        GamePainter
{
    public final static String PAINTERS_NAME = "painters";

    private static final double SCROLLSPEED = 1;
    private final Activity activity;
    private final GamePainter[][] painters;
    private final GameState gameState;
    private ArrayList<GamePainter[]> gameObjects;
    private ArrayList<GamePainter[]> fixedPositionObjects;
    private long lastRun;


    /**
     * Simple constructor to use when creating a view from code.
     *
     * @param activity Get the Context the view is running in, through which it can
     *                access the current theme, resources, etc.
     */
    @DepWire
    public GraphicsManager(@Inject("activity") final Activity activity,
                           @Inject(PAINTERS_NAME) GamePainter[][] painters,
                           @Inject("GameState") GameState gameState)
    {
        super(activity);

        this.activity = activity;
        this.painters = painters;
        this.gameState = gameState;

        this.fixedPositionObjects = new ArrayList<>();
        this.gameObjects = new ArrayList<>();

        this.lastRun = System.nanoTime();

        /**
         * First element of the array is stationary objects
         * The rest are moving parts of the game.
         */
        int i = 0;
        for (GamePainter[] layerPainters : this.painters) {
            if(i==0) {
                fixedPositionObjects.add(layerPainters);
            }
            else {
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
        this.paint(canvas, this.getScrollY());

        this.updateScrollY();
    }

    /**
     * Draw objects that are a part of the game
     */
    @Override
    public void paint(final Canvas canvas)
    {
        for (GamePainter[] layerPainters : this.gameObjects) {
            for (GamePainter painter : layerPainters) {
                painter.paint(canvas);
            }
        }

    }

    /**
     * Draw Objects that are fixed
     */
    @Override
    public void paint(Canvas canvas, float y)
    {
        for (GamePainter[] layerPainters : this.fixedPositionObjects) {
            for (GamePainter painter : layerPainters) {
                painter.paint(canvas,y);
            }
        }
    }

    /**
     * Updates the Scroll Value
     * And saves the updated value in gameState
     */
    private void updateScrollY()
    {
        setNewScrollValue();
        this.gameState.setYScrollValue(this.getScrollY());
    }

    /**
     * Update the Scrolling of the Floors based on time since last update.
     * This way it should be quite similar when playing multiplayer.
     */
    public void setNewScrollValue()
    {
        long now = System.nanoTime();

        double diff = (now - this.lastRun) / 10000000.0;
        double update = diff * SCROLLSPEED;

        if(update > 1) {
            this.lastRun = now;
            this.scrollBy(0, (int) -update);
        }
    }
}
