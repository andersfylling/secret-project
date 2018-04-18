package team.adderall.game.framework;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.view.View;

import java.util.ArrayList;

import team.adderall.game.framework.component.GameComponent;
import team.adderall.game.framework.component.GameDepWire;
import team.adderall.game.framework.component.Inject;
import team.adderall.game.framework.context.GameContext;

@GameComponent
public class GamePaintWrapper
        extends
        View
        implements
        GamePainter
{
    private final Activity activity;
    private final GamePainter[][] painters;
    private ArrayList<GamePainter[]> gameObjects;
    private ArrayList<GamePainter[]> fixedPositionObjects;


    /**
     * Simple constructor to use when creating a view from code.
     *
     * @param activity Get the Context the view is running in, through which it can
     *                access the current theme, resources, etc.
     */
    @GameDepWire
    public GamePaintWrapper(
            @Inject("activity") final Activity activity,
            @Inject(GameContext.PAINT) GamePainter[][] painters
    ) {
        super(activity);

        this.activity = activity;
        this.painters = painters;
        this.fixedPositionObjects = new ArrayList<GamePainter[]>();
        this.gameObjects = new ArrayList<GamePainter[]>();

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
        final GamePaintWrapper self = this;
        this.activity.runOnUiThread(() -> self.activity.setContentView(self));
    }

    public void redraw() {
        final GamePaintWrapper self = this;
        this.activity.runOnUiThread(self::invalidate);
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        this.paint(canvas);
        this.paint(canvas,this.getScrollY());
    }

    @Override
    /**
     * Draw objects that are a part of the game
     */
    public void paint(final Canvas canvas) {

        // Todo: make scrolling time dependent. Such that it would syncronice on all devices.
        this.scrollBy(0,-1);

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
    public void paint(Canvas canvas, float y) {
        for (GamePainter[] layerPainters : this.fixedPositionObjects) {
            for (GamePainter painter : layerPainters) {
                painter.paint(canvas,y);
            }
        }
    }
}
