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

    public static void jump(Player player) {
        if(player != null && player.getBallManager().getAtGround()) {
            player.getBallManager().setVelocity(JUMP_VELOCITY * Gravity.METER);
            player.getBallManager().setAtGround(false);
        }
    }

    @Override
    public void run() {
        jump(this.player);
    }
}
