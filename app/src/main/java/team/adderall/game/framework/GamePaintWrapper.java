package team.adderall.game.framework;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.view.View;

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
    }

    @Override
    public void paint(final Canvas canvas) {

        this.scrollBy(0,-1);

        for (GamePainter[] layerPainters : this.painters) {
            for (GamePainter painter : layerPainters) {
                painter.paint(canvas);
            }
        }
    }
}
