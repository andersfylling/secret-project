package team.adderall.game.framework.gameconfigs;

import team.adderall.game.framework.Logicer;
import team.adderall.game.framework.component.GameComponent;
import team.adderall.game.framework.component.Name;

public class GameComponentsWithSelfDepCycling {
    // this should throw an instantiation error
    @GameComponent
    public Logicer throwErrorPleaseA(@Name("throwErrorPleaseA") Logicer a) {
        return null;
    }
}
