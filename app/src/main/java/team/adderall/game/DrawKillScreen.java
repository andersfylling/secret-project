package team.adderall.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import team.adderall.game.ball.BallManager;
import team.adderall.game.framework.GamePainter;
import team.adderall.game.framework.component.GameComponent;
import team.adderall.game.framework.component.GameDepWire;
import team.adderall.game.framework.component.Inject;

/**
 * Created by Cim on 18/4/18.
 */
@GameComponent("DrawKillScreen")
public class DrawKillScreen implements GamePainter {

    private final Players players;
    private final Paint painter;
    private final GameState gameState;
    private final Paint textPainter;

    @GameDepWire
    public DrawKillScreen(
            @Inject("players") Players players,
            @Inject("GameState") GameState gameState
    ){
        this.players = players;
        this.painter = new Paint();
        this.textPainter = new Paint();
        this.textPainter.setColor(Color.RED);
        this.textPainter.setTextSize(75);
        this.textPainter.setTextAlign(Paint.Align.CENTER);

        this.gameState = gameState;
    }

    @Override
    public void paint(Canvas canvas) {
        /**
         * TODO: Should only be done for you as a player?
         */
        for(BallManager b : players.toList()) {
            if (b.getState() == BallManager.STATE_DEAD) {
                int extraY = (int) gameState.getyScaleValue();
                this.painter.setColor(Color.BLACK);
                this.painter.setAlpha(200);

                canvas.drawRect(0, 0 + extraY, canvas.getWidth(), canvas.getHeight()+ extraY, this.painter);
                canvas.drawText("YOU ARE DEAD", canvas.getWidth() / 2,  (canvas.getHeight() / 2) + extraY, this.textPainter);
            }
        }
    }

    @Override
    public void paint(Canvas canvas, float y) {

    }
}
