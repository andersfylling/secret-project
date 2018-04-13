package team.adderall.game.framework.gameconfigs;

import team.adderall.game.framework.configuration.GameConfiguration;
import team.adderall.game.framework.component.GameComponent;
import team.adderall.game.framework.component.Inject;

@GameConfiguration
public class GameComponentsWithUnknownDep {
    public final static int NUM_OF_GAME_COMPONENTS = 1;

    @GameComponent
    public String gameLogicWithParams(@Inject("thisNameShouldNotExist") final Object t){
        return "test";
    }
}
