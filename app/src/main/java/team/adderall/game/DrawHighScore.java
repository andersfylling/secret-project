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

    @GameDepWire
    public DrawHighScore(@Inject("players") Players p)
    {
        this.players = p;
    }


    @Override
    public void paint(Canvas canvas) {

    }

    @Override
    public void paint(Canvas canvas, float y) {
        for(BallManager player : players.toList()){
            player.drawHighScore(canvas,y);
        }
    }
}
