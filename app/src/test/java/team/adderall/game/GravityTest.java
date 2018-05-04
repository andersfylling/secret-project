package team.adderall.game;

import org.junit.Test;

import team.adderall.game.physics.Gravity;
import team.adderall.game.physics.GravityAffected;

import static org.junit.Assert.assertEquals;

public class GravityTest {

    @Test
    public void validateGravityLogic() {
        Gravity gravity = new Gravity(null, null);

        GravityAffected entity = new GravityAffected() {
            private double y = 0;
            private double velocity = 0;

            @Override
            public double y() {
                return 0;
            }

            @Override
            public void y(double y) {
                this.y = y;
            }

            @Override
            public double velocity() {
                return 0;
            }

            @Override
            public void velocity(double v) {
                this.velocity = v;
            }
        };

        double acceleration = 20;
        gravity.updateGravityAffected(entity, acceleration, 1); // 20pixels, 1second

        assertEquals(acceleration, entity.y(), 100);
        assertEquals(acceleration, entity.velocity(), 100);

        // ensure that values are updated correctly over time
        gravity.updateGravityAffected(entity, acceleration, 0.5);

        assertEquals(acceleration + 0.5*acceleration, entity.y(), 100);
        assertEquals(2*acceleration, entity.velocity(), 100);
    }
}
