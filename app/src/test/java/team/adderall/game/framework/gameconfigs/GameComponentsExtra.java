package team.adderall.game.framework.gameconfigs;

import team.adderall.game.framework.GameConfiguration;
import team.adderall.game.framework.Logicer;
import team.adderall.game.framework.component.GameComponent;
import team.adderall.game.framework.component.Inject;

import static org.junit.Assert.assertEquals;

@GameConfiguration
public class GameComponentsExtra {
    public final static int NUM_OF_GAME_COMPONENTS = 2;

    private String gameLogicWithParamExecuted2 = "";

    public void testingReflection() {}

    @GameComponent
    public int getASeven() {
        return 7;
    }
    @GameComponent
    public Object ensurePrivacyBetweenConfigs(@Inject("gameLogicWithParams") Logicer t) {
        assertEquals(this.gameLogicWithParamExecuted2, "");
        t.run();
        assertEquals(this.gameLogicWithParamExecuted2, "");

        return "test";
    }
}
