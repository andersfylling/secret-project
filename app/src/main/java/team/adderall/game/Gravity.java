package team.adderall.game;

import android.graphics.Point;

import java.util.concurrent.TimeUnit;

import team.adderall.game.ball.BallManager;
import team.adderall.game.framework.GameLogicInterface;
import team.adderall.game.framework.GameLoop;
import team.adderall.game.framework.component.GameComponent;
import team.adderall.game.framework.component.GameDepWire;
import team.adderall.game.framework.component.GameLogic;
import team.adderall.game.framework.component.Inject;

@GameComponent
@GameLogic(wave = 2)
public class Gravity
    implements GameLogicInterface
{
    public final static double METER = 100;
    public final static double GRAVITY = 9.8 * METER;

    private final Players players;
    private DeltaTime deltaTime;

    @GameDepWire
    public Gravity(@Inject("deltaTime") DeltaTime deltaTime,
                   @Inject("players") Players p)
    {
        this.players = p;
        this.deltaTime = deltaTime;
    }

    @Override
    public void run() {
        double diff = deltaTime.getDiff(TimeUnit.SECONDS);
        double acceleration = GRAVITY * diff;

        for(Player player : players.getAlivePlayers()){
            BallManager b = player.getBallManager(); // holds position info
            double velocity = b.getVelocity();
            velocity += acceleration;

            b.setY(b.getY() + (velocity * diff));
            b.setVelocity(velocity);
        }
    }
}
