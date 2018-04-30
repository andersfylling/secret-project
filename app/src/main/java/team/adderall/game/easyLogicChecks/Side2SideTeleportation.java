package team.adderall.game.easyLogicChecks;

import android.graphics.Point;

import com.google.android.gms.games.Game;

import team.adderall.game.GameState;
import team.adderall.game.Player;
import team.adderall.game.Players;
import team.adderall.game.ball.Ball;
import team.adderall.game.ball.BallManager;
import team.adderall.game.framework.GameLogicInterface;
import team.adderall.game.framework.component.GameComponent;
import team.adderall.game.framework.component.GameDepWire;
import team.adderall.game.framework.component.GameLogic;
import team.adderall.game.framework.component.Inject;

@GameComponent
@GameLogic(wave = 4)
public class Side2SideTeleportation implements GameLogicInterface {
    private final Players players;
    private int width;

    @GameDepWire
    public Side2SideTeleportation(@Inject("players") Players players)
    {
        this.players = players;
        this.width = GameState.FIXED_WIDTH;
    }

    @Override
    public void run() {

        for(Player player : players.getAlivePlayers()){
            BallManager bm = player.getBallManager();
            double x = bm.getX();

            if(x <= 0){
                bm.setPos(x+width, bm.getY());
            }
            else if(x >= width){
                bm.setPos(x%width, bm.getY());
            }
        }
    }
}
