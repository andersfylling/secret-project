package team.adderall.game;

import team.adderall.game.framework.component.GameComponent;
import team.adderall.game.framework.component.GameDepWire;

/**
 * Keep track of changes.
 * Make other components be able to register actions on specific changes. death, reborn, etc.
 */
@GameComponent("playerStateTracker")
public class PlayerStateTracker {



    //private

    @GameDepWire
    public PlayerStateTracker() {}
}
