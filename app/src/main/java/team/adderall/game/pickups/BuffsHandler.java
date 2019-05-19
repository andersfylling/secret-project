package team.adderall.game.pickups;

import android.graphics.Color;

import java.util.ArrayList;

import addy.annotations.*;
import team.adderall.game.gameloop.GameLogic;
import team.adderall.game.player.Player;
import team.adderall.game.player.Players;
import team.adderall.game.ball.BallManager;
import team.adderall.game.gameloop.GameLogicInterface;
import team.adderall.game.level.Floor;
import team.adderall.game.level.LevelManager;


@Service
@GameLogic(wave = 3)
public class BuffsHandler
        implements
        GameLogicInterface
{
    private final Players players;
    private final LevelManager level;
    private ArrayList<Buff> buffs;

    @DepWire
    public BuffsHandler(@Inject("players") Players p,
                        @Inject("level") LevelManager level)
    {
        this.players = p;
        this.level = level;
        this.buffs = new ArrayList<>();

        /**
         * Initialise Buff types
         */
        Buff singleCoin = new ExtraPointBuff(1,5,5, Color.YELLOW);
        buffs.add(singleCoin);

        Buff superCoin = new ExtraPointBuff(2,500,20, Color.WHITE);
        buffs.add(superCoin);
        level.setBuffs(buffs);
    }


    public ArrayList<Buff> getBuffs(){
        return this.buffs;
    }


    @Override
    public void run() {
        for (Player p : players.getAlivePlayers()) {
            BallManager player = p.getBallManager();

            double x = player.getX();
            double y = player.getY();

            for (Floor floor : level.getFloors()) {
                int type = floor.aidColision((int) x, (int) y);
                if (type == Floor.NOT_COLLIDING) {
                    continue;
                }

                /*
                 * Handle aid objectives here.
                 */
                for (Buff buff : this.buffs) {
                    if (buff.getType() == type) {
                        buff.handleCollision(player);
                    }
                }

            }
        }
    }
}
