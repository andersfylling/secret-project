package team.adderall.game;

import android.graphics.Canvas;

import team.adderall.game.ball.BallManager;
import team.adderall.game.framework.GamePainter;
import team.adderall.game.framework.component.GameComponent;
import team.adderall.game.framework.component.GameDepWire;
import team.adderall.game.framework.component.Inject;

/**
 * Created by Cim on 14/4/18.
 */
@GameComponent("DrawHighScore")
public class DrawHighScore implements
        GamePainter{
    private final Players players;
    private final GameState gameState;

    @GameDepWire
    public DrawHighScore(@Inject("players") Players p,
                         @Inject("GameState") GameState gameState)
    {
        this.players = p;
        this.gameState = gameState;
    }


    @Override
    public void paint(Canvas canvas) {
        int y = (int) gameState.getyScaleValue();
        for(Player player : players.getAlivePlayersAsList()){
            player.getBallManager().drawHighScore(canvas,y);
        }
    }

    @Override
    public void paint(Canvas canvas, float y) {

    }
}
