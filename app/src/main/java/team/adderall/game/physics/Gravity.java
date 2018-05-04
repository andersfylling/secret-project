package team.adderall.game.physics;

import java.util.concurrent.TimeUnit;

import team.adderall.game.Players;
import team.adderall.game.framework.GameLogicInterface;
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

    /**
     * Update a entity position after gravity influence
     * @param entity
     * @param acceleration
     * @param delta
     */
    public void updateGravityAffected(GravityAffected entity, double acceleration, double delta) {
        double velocity = entity.velocity() + acceleration;
        double position = entity.y() + (delta * velocity);

        entity.y(position);
        entity.velocity(velocity);
    }


    /**
     * Calculate how gravity affects all GravityAffected entity.
     */
    @Override
    public void run() {
        double diff = deltaTime.getDiff(TimeUnit.SECONDS);
        double acceleration = GRAVITY * diff;

        // players
        // since we don't care about updating alive players
        // we let the Players class keep track of state for us
        for(GravityAffected entity : players.getAlivePlayers()){
            updateGravityAffected(entity, acceleration, diff);
        }
    }
}
