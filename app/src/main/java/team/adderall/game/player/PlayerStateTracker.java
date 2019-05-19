package team.adderall.game.player;

import addy.annotations.*;

/**
 * Keep track of changes.
 * Make other components be able to register actions on specific changes. death, reborn, etc.
 */
@Service
@Deprecated
public class PlayerStateTracker {



    //private

    @DepWire
    public PlayerStateTracker() {}
}
