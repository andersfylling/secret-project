package team.adderall.game;

import android.graphics.Point;

import team.adderall.game.ball.BallManager;
import team.adderall.game.framework.GameLogicInterface;
import team.adderall.game.framework.component.GameComponent;
import team.adderall.game.framework.component.GameDepWire;
import team.adderall.game.framework.component.GameLogic;
import team.adderall.game.framework.component.Inject;

@GameComponent
@GameLogic(wave = 3)
public class KillPlayerWhenBelowScreen
    implements GameLogicInterface
{

    private final GameState gameState;
    private Players players;
    private int deathLine;

    @GameDepWire
    public KillPlayerWhenBelowScreen(
            @Inject("players") Players players,
            @Inject("canvasSize") Point canvasSize,
            @Inject("GameState") GameState gameState
    ) {
        this.players = players;
        this.deathLine = canvasSize.y;
        this.gameState = gameState;
    }


    @Override
    public void run() {
        int realDeathLine = (int) (gameState.getyScaleValue() + this.deathLine);
        for (Player player : this.players.getAlivePlayers()) {
            BallManager b = player.getBallManager();
            if (b.getPos().y > realDeathLine) {
                b.setState(BallManager.STATE_DEAD);
            }
        }
    }
}
