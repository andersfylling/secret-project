package team.adderall.game;

import android.content.Intent;
import android.graphics.Point;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.io.Serializable;

import team.adderall.GameActivity;
import team.adderall.R;
import team.adderall.fragments.HighScoreFragment;
import team.adderall.game.ball.BallManager;
import team.adderall.game.framework.GameLogicInterface;
import team.adderall.game.framework.component.GameComponent;
import team.adderall.game.framework.component.GameDepWire;
import team.adderall.game.framework.component.GameLogic;
import team.adderall.game.framework.component.Inject;

import static java.security.AccessController.getContext;

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
                if(b.isActivePlayer())
                    showHighScore();

            }
        }
    }

    private void showHighScore() {
        GameActivity gm = GameActivity.getActivity();
        if(gm != null){
            gm.weDied();
        }

    }

}
