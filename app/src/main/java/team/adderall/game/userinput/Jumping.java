package team.adderall.game.userinput;

import team.adderall.game.Gravity;
import team.adderall.game.Player;
import team.adderall.game.Players;
import team.adderall.game.framework.component.*;

@GameComponent
public class Jumping
    implements Runnable
{
    public final static int JUMP_VELOCITY = -11;
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
            this.player.getBallManager().setVelocity(JUMP_VELOCITY * Gravity.METER);
            this.player.getBallManager().setAtGround(false);
        }
    }
}
