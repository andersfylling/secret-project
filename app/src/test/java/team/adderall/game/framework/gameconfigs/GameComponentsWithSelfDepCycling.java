package team.adderall.game.framework.gameconfigs;

import team.adderall.game.framework.configuration.GameConfiguration;
import team.adderall.game.framework.GameLogicInterface;
import team.adderall.game.framework.component.GameComponent;
import team.adderall.game.framework.component.Inject;

@GameConfiguration
public class GameComponentsWithSelfDepCycling {
    public final static int NUM_OF_GAME_COMPONENTS = 1;
    // this should throw an instantiation error
    @GameComponent
    public GameLogicInterface throwErrorPleaseA(@Inject("throwErrorPleaseA") GameLogicInterface a) {
        return null;
    }
}
