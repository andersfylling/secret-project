package team.adderall.game.userinput;

import team.adderall.game.physics.Gravity;
import team.adderall.game.Player;

public class Jumping
{
    public final static int JUMP_VELOCITY = -11;

    public Jumping()
    {}

    public static void jump(Player player) {
        if(player != null && player.getBallManager().getAtGround()) {
            player.getBallManager().setVelocity(JUMP_VELOCITY * Gravity.METER);
            player.getBallManager().setAtGround(false);
        }
    }
}
