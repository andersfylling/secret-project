package team.adderall.game.GameExtraObjects;

import android.graphics.Color;
import android.graphics.Point;

import java.util.ArrayList;

import team.adderall.game.Player;
import team.adderall.game.Players;
import team.adderall.game.ball.BallManager;
import team.adderall.game.framework.GameLogicInterface;
import team.adderall.game.framework.component.GameComponent;
import team.adderall.game.framework.component.GameDepWire;
import team.adderall.game.framework.component.Inject;
import team.adderall.game.level.Floor;
import team.adderall.game.level.LevelManager;


@GameComponent
public class AidsHandler implements GameLogicInterface {
    private final Players players;
    private final LevelManager level;
    private ArrayList<Aid> aids;

    @GameDepWire
    public AidsHandler(@Inject("players") Players p, @Inject("level") LevelManager level) {
        this.players = p;
        this.level = level;
        this.aids = new ArrayList<>();

        /**
         * Initialise Aid types
         */
        Aid singleCoin = new ExtraPointAid(1,5,5, Color.YELLOW);
        aids.add(singleCoin);

        Aid superCoin = new ExtraPointAid(2,500,20, Color.WHITE);
        aids.add(superCoin);
        level.setAids(aids);
    }


    public ArrayList<Aid> getAids(){
        return this.aids;
    }



    @Override
    public void run() {
        Player p = this.players.getActive();
        BallManager player = p.getBallManager();

        Point pos = player.getPos();

        for (Floor floor : level.getFloors()) {
            int type = floor.aidColision(pos);

            if (type != 0) {
                /**
                 * Handle aid objectives here.
                 */
                for (Aid aid : this.aids) {
                    if (aid.getType() == type) {
                        aid.handleCollision(player);
                    }
                }
            }

        }
    }
}
