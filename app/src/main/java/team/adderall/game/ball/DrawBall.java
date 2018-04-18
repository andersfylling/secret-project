package team.adderall.game.ball;

import android.graphics.Canvas;

import team.adderall.game.Players;
import team.adderall.game.framework.GameLogicInterface;
import team.adderall.game.framework.GamePainter;
import team.adderall.game.framework.component.GameComponent;
import team.adderall.game.framework.component.GameDepWire;
import team.adderall.game.framework.component.GameLogic;
import team.adderall.game.framework.component.Inject;

/**
 * Created by Cim on 14/4/18.
 */
@GameComponent("DrawBall")
public class DrawBall implements
        GamePainter{
    private final Players players;

    @GameDepWire
    public DrawBall(@Inject("players") Players p)
    {
        this.players = p;
    }


    @Override
    public void paint(Canvas canvas) {
        for(BallManager player : players.toList()){
            player.paint(canvas);
        }
    }

    @Override
    public void paint(Canvas canvas, float y) {

    }
}
