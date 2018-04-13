package team.adderall.game.framework.gameconfigs;

import team.adderall.game.framework.GameConfiguration;
import team.adderall.game.framework.Logicer;
import team.adderall.game.framework.component.GameComponent;
import team.adderall.game.framework.component.Name;

@GameConfiguration
public class GameComponentsWithUnknownDep {
    public final static int NUM_OF_GAME_COMPONENTS = 1;

    @GameComponent
    public String gameLogicWithParams(@Name("thisNameShouldNotExist") final Object t){
        return "test";
    }
}
