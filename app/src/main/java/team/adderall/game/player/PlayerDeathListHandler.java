package team.adderall.game.player;

import addy.annotations.*;
import team.adderall.game.gameloop.GameLogic;
import team.adderall.game.player.Player;
import team.adderall.game.player.Players;
import team.adderall.game.ball.BallManager;
import team.adderall.game.gameloop.GameLogicInterface;

@Service
@GameLogic(wave = 4)
public class PlayerDeathListHandler
     implements GameLogicInterface
{
    private final Players players;

    @DepWire
    public PlayerDeathListHandler(@Inject("players") Players p)
    {
        this.players = p;
    }

    /**
     * Move any players away from the active pile, if they are actually dead.
     */
    @Override
    public void run() {
        for (Player player : this.players.getAlivePlayers()) {
            if(player.getBallManager().getState() == BallManager.STATE_DEAD){
                players.setToDead(player);
            }
        }
    }
}
