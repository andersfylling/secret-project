package team.adderall.game.ball;

import android.graphics.Canvas;

import team.adderall.game.GameState;
import team.adderall.game.player.Player;
import team.adderall.game.player.Players;
import team.adderall.game.gameloop.GamePainter;
import addy.annotations.*;

@Service
public class DrawBall
        implements
        GamePainter
{
    private final Players players;
    private final GameState gameState;

    @DepWire
    public DrawBall(@Inject("players") Players p,
                    @Inject("GameState") GameState gameState)
    {
        this.players = p;
        this.gameState = gameState;
    }


    @Override
    public void paint(Canvas canvas)
    {
        for(Player player : players.getAlivePlayers()){
            player.getBallManager().paint(canvas, gameState.getXScale());
        }
    }

    @Override
    public void paint(Canvas canvas, float y)
    {}
}
