package team.adderall.game.userinput;

import team.adderall.game.Gravity;
import team.adderall.game.Player;
import team.adderall.game.Players;
import team.adderall.game.framework.component.*;

@GameComponent
public class Jumping
    implements Runnable
{
    private final Player player;

    @GameDepWire
    public Jumping(
            @Inject("players") Players players
    ) {
        this.player = players.getActive();
    }

    @Override
    public void run() {
        if(this.player != null && this.player.getBallManager().getAtGround()) {
<<<<<<< HEAD
            this.player.getBallManager().setVelocity(-11 * 100);
=======
            this.player.getBallManager().setVelocity(-11 * Gravity.METER);
>>>>>>> befd36e2a2befbacc4f2cab55ba1df424445653d
            this.player.getBallManager().setAtGround(false);
        }
    }
}
