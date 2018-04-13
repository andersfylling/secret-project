package team.adderall.game.framework.gameconfigs;

import team.adderall.game.framework.Logicer;
import team.adderall.game.framework.component.GameComponent;
import team.adderall.game.framework.component.Name;

public class GameComponentsExtra {

    private String gameLogicWithParamExecuted2 = "";

    public void testingReflection() {}

    @GameComponent
    public int getASeven() {
        return 7;
    }
    @GameComponent
    public Object ensurePrivacyBetweenConfigs(@Name("gameLogicWithParams") Logicer t) {
        assert(this.gameLogicWithParamExecuted2.equals(""));
        t.run();
        assert(this.gameLogicWithParamExecuted2.equals(""));

        return "test";
    }
}
