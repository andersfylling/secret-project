package team.adderall.game.easyLogicChecks;

import android.graphics.Point;

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
public class Side2SideTeleportation implements GameLogicInterface {
    private final Players players;
    private int width;

    @GameDepWire
    public Side2SideTeleportation(@Inject("players") Players p, int x){
        this.players = p;
        this.width = x;
    }

    @Override
    public void run() {
        for(Player player : players.getAlivePlayersAsList()){
            BallManager bm = player.getBallManager();
            Point p = bm.getPos();

            int radius = bm.getBall().getRadius();
            if(p.x + radius < 0){
                p.set(p.x +width,p.y);
                bm.setPos(p);
            }
            else if(p.x - radius > width){
                p.set(p.x%width,p.y);
                bm.setPos(p);
            }
        }
    }
}
