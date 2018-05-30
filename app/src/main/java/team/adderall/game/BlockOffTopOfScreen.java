package team.adderall.game;

import addy.annotations.*;
import team.adderall.game.ball.BallManager;
import team.adderall.game.gameloop.GameLogic;
import team.adderall.game.gameloop.GameLogicInterface;
import team.adderall.game.player.Player;
import team.adderall.game.player.Players;

@Service
@GameLogic(wave = 3)
public class BlockOffTopOfScreen
        implements
        GameLogicInterface
{
    // This makes sure that parts of the ball object
    // isn't outside the top of the screen.
    private final static int SAFETY_MARGIN = 15;

    private final Players players;
    private final GameState gameState;

    @DepWire
    public BlockOffTopOfScreen(@Inject("players") Players p,
                               @Inject("GameState") GameState gs)
    {
        players = p;
        gameState = gs;
    }

    @Override
    public void run() {
        double heightLimit = gameState.getYScrollValue();
        for (Player player : this.players.getAlivePlayers()) {
            BallManager b = player.getBallManager();
            if (b.getY() - b.getBall().getRadius() - SAFETY_MARGIN < heightLimit) {
                b.setVelocity(0);
                b.setY(heightLimit + b.getBall().getRadius() + SAFETY_MARGIN);
            }
        }
    }
}
