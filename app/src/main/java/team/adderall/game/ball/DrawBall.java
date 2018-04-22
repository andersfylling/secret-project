package team.adderall.game.ball;

import android.graphics.Canvas;

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

    @GameDepWire
    public DrawBall(@Inject("players") Players p)
    {
        this.players = p;
    }


    @Override
    public void paint(Canvas canvas)
    {
        for(Player player : players.getAlivePlayersAsList()){
            player.getBallManager().paint(canvas);
        }
    }

    @Override
    public void paint(Canvas canvas, float y)
    {}
}
