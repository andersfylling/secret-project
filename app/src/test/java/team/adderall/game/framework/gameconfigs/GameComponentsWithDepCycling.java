package team.adderall.game.framework.gameconfigs;

import team.adderall.game.framework.configuration.GameConfiguration;
import team.adderall.game.framework.GameLogicInterface;
import team.adderall.game.framework.component.GameComponent;
import team.adderall.game.framework.component.Inject;

@GameConfiguration
public class GameComponentsWithDepCycling {
    public final static int NUM_OF_GAME_COMPONENTS = 2;
    // this should throw an instantiation error
    @GameComponent
    public GameLogicInterface throwErrorPleaseB(@Inject("throwErrorPleaseC") GameLogicInterface c) {
        return null;
    }
    @GameComponent
    public GameLogicInterface throwErrorPleaseC(@Inject("throwErrorPleaseB") GameLogicInterface b) {
        return null;
    }
}
