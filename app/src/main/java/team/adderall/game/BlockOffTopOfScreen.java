package team.adderall.game;

import team.adderall.game.ball.BallManager;
import team.adderall.game.framework.GameLogicInterface;
import team.adderall.game.framework.component.GameComponent;
import team.adderall.game.framework.component.GameDepWire;
import team.adderall.game.framework.component.GameLogic;
import team.adderall.game.framework.component.Inject;

@GameComponent
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

    @GameDepWire
    public BlockOffTopOfScreen(@Inject("players") Players p,
                               @Inject("GameState") GameState gs)
    {
        players = p;
        gameState = gs;
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        double heightLimit = gameState.getyScrollValue();
        for (Player player : this.players.getAlivePlayers()) {
            BallManager b = player.getBallManager();
            if (b.getY() - b.getBall().getRadius() - SAFETY_MARGIN < heightLimit) {
                b.setVelocity(0);
                b.setY(heightLimit + b.getBall().getRadius() + SAFETY_MARGIN);
            }
        }
    }
}
