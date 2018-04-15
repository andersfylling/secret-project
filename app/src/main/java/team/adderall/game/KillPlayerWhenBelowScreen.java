package team.adderall.game;

import android.graphics.Point;
import android.view.Display;

import team.adderall.game.ball.BallManager;
import team.adderall.game.framework.GameLogicInterface;
import team.adderall.game.framework.component.GameComponent;
import team.adderall.game.framework.component.GameDepWire;
import team.adderall.game.framework.component.Inject;

@GameComponent("killPlayerWhenBelowScreen")
public class KillPlayerWhenBelowScreen
    implements GameLogicInterface
{

    private Players players;
    private int deathLine;

    @GameDepWire
    public KillPlayerWhenBelowScreen(
            @Inject("players") Players players,
            @Inject("canvasSize") Point canvasSize
    ) {
        this.players = players;
        this.deathLine = canvasSize.y;
    }


    @Override
    public void run() {
        for (BallManager b : this.players.toList()) {
            if (b.getPos().y > this.deathLine) {
                b.setState(BallManager.STATE_DEAD);
            }
        }
    }
}
