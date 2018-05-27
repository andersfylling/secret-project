package team.adderall.game.player;

import addy.annotations.*;
import team.adderall.game.gameloop.GameLogic;
import team.adderall.game.GameState;
import team.adderall.game.player.Player;
import team.adderall.game.player.Players;
import team.adderall.game.ball.BallManager;
import team.adderall.game.gameloop.GameLogicInterface;


@Service
@GameLogic(wave = 4)
public class Side2SideTeleportation implements GameLogicInterface {
    private final Players players;
    private int width;

    @DepWire
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
