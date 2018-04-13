package team.adderall.game.framework;

import team.adderall.game.framework.component.GameComponent;
import team.adderall.game.framework.component.Name;

public class GameComponentsWithDepCycling {
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
