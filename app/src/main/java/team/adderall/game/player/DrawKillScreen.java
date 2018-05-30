package team.adderall.game.player;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import team.adderall.game.gameloop.GamePainter;
import team.adderall.game.GameState;
import team.adderall.game.ball.BallManager;
import addy.annotations.*;

@Service
public class DrawKillScreen
        implements
        GamePainter
{

    private final Players players;
    private final Paint painter;
    private final GameState gameState;
    private final Paint textPainter;

    @DepWire
    public DrawKillScreen(@Inject("players") Players players,
                          @Inject("GameState") GameState gameState)
    {
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
        Player player = players.getActive();
        if (player == null) {
            return;
        }

        if (player.getBallManager().getState() != BallManager.STATE_DEAD) {
            return;
        }

        int extraY = (int) gameState.getYScrollValue();
        this.painter.setColor(Color.BLACK);
        this.painter.setAlpha(200);

        canvas.drawRect(0, extraY, canvas.getWidth(), canvas.getHeight()+ extraY, this.painter);
        canvas.drawText("YOU ARE DEAD", canvas.getWidth() / 2,  (canvas.getHeight() / 2) + extraY, this.textPainter);
    }

    @Override
    public void paint(Canvas canvas, float y) {

    }
}
