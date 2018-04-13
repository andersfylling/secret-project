package team.adderall.game.framework.gameconfigs;

import team.adderall.game.framework.GameConfiguration;
import team.adderall.game.framework.Logicer;
import team.adderall.game.framework.component.GameComponent;
import team.adderall.game.framework.component.Name;

public class GameComponentsWithoutGameConfigAnnotation {
    public final static int NUM_OF_GAME_COMPONENTS = 2;

    private String gameLogicWithParamExecuted2 = "";

    public void testingReflection() {}

    @GameComponent
    public int getASevenaa() {
        return 7;
    }
    @GameComponent
    public Object ensurePrivacyBetweenConfigsaa(@Name("gameLogicWithParams") Logicer t) {
        assert(this.gameLogicWithParamExecuted2.equals(""));
        t.run();
        assert(this.gameLogicWithParamExecuted2.equals(""));

        return "test";
    }
}
