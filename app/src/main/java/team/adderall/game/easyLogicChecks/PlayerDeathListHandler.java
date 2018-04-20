package team.adderall.game.easyLogicChecks;

import team.adderall.game.Player;
import team.adderall.game.Players;
import team.adderall.game.ball.Ball;
import team.adderall.game.ball.BallManager;
import team.adderall.game.framework.GameLogicInterface;
import team.adderall.game.framework.component.GameComponent;
import team.adderall.game.framework.component.GameDepWire;
import team.adderall.game.framework.component.Inject;

/**
 * Created by Cim on 19/4/18.
 */

@GameComponent
public class PlayerDeathListHandler
     implements GameLogicInterface

    {
        private final Players players;

        @GameDepWire
        public PlayerDeathListHandler(@Inject("players") Players p){
            this.players = p;
        }

        @Override
        /**
         * Move any players away from the active pile, if they are actually dead.
         */
        public void run() {
            for (Player player : this.players.getAlivePlayersAsList()) {
                if(player.getBallManager().getState() != BallManager.STATE_DEAD){
                    continue;
                }

                players.setToDead(player.getGameID());
            }
        }
    }
