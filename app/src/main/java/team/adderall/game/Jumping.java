package team.adderall.game;

import team.adderall.game.framework.component.*;

@GameComponent("jumping")
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
            this.player.getBallManager().setVelocity(-5);
            this.player.getBallManager().setAtGround(false);
        }
    }
}
