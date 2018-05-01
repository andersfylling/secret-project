package team.adderall.game.ball;

import android.graphics.Canvas;

import team.adderall.game.GameState;
import team.adderall.game.Player;
import team.adderall.game.Players;
import team.adderall.game.framework.GameLogicInterface;
import team.adderall.game.framework.GamePainter;
import team.adderall.game.framework.component.GameComponent;
import team.adderall.game.framework.component.GameDepWire;
import team.adderall.game.framework.component.GameLogic;
import team.adderall.game.framework.component.Inject;

@GameComponent
public class DrawBall
        implements
        GamePainter
{
    private final Players players;
    private final GameState gameState;

    @GameDepWire
    public DrawBall(
            @Inject("players") Players p,
            @Inject("GameState") GameState gameState
    )
    {
        this.players = p;
        this.gameState = gameState;

    }


    @Override
    public void paint(Canvas canvas)
    {
        for(Player player : players.getAlivePlayers()){
            player.getBallManager().paint(canvas, gameState.getxScale());
        }
    }

    @Override
    public void paint(Canvas canvas, float y)
    {}
}
