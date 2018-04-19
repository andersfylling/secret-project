package team.adderall.game;

import team.adderall.game.ball.BallManager;
import team.adderall.game.framework.component.GameComponent;
import team.adderall.game.framework.component.GameDepWire;
import team.adderall.game.framework.component.Inject;

@GameComponent("jumping")
public class Jumping
    implements Runnable
{
    private final BallManager player;

    @GameDepWire
    public Jumping(
            @Inject("players") Players players
    ) {
        this.player = players.getActive();
    }

    @Override
    public void run() {
        if(this.player.getAtGround()) {
            this.player.setVelocity(-5);
            this.player.setAtGround(false);
        }
    }
}
