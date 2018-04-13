package team.adderall.game.framework.gameconfigs;

import team.adderall.game.framework.GameConfiguration;
import team.adderall.game.framework.Logicer;
import team.adderall.game.framework.component.GameComponent;
import team.adderall.game.framework.component.Name;

@GameConfiguration
public class GameComponentsWithSelfDepCycling {
    public final static int NUM_OF_GAME_COMPONENTS = 1;
    // this should throw an instantiation error
    @GameComponent
    public Logicer throwErrorPleaseA(@Name("throwErrorPleaseA") Logicer a) {
        return null;
    }
}
