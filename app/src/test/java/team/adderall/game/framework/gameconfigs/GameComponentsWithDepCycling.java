package team.adderall.game.framework.gameconfigs;

import team.adderall.game.framework.Logicer;
import team.adderall.game.framework.component.GameComponent;
import team.adderall.game.framework.component.Name;

public class GameComponentsWithDepCycling {
    public final static int NUM_OF_GAME_COMPONENTS = 2;
    // this should throw an instantiation error
    @GameComponent
    public Logicer throwErrorPleaseB(@Name("throwErrorPleaseC") Logicer c) {
        return null;
    }
    @GameComponent
    public Logicer throwErrorPleaseC(@Name("throwErrorPleaseB") Logicer b) {
        return null;
    }
}
