package team.adderall.game.physics;

import team.adderall.game.physics.XYPosition;
import team.adderall.game.physics.Velocity;

/**
 * Any entity that can cause collision by moving around, such as players.
 */
public interface Collidable
        extends
        XYPosition,
        Velocity
{
    void setAtGround(boolean atGround);
    boolean getAtGround();
}
