package team.adderall.game.framework.gameconfigs;

import team.adderall.game.framework.GameConfiguration;
import team.adderall.game.framework.Logicer;
import team.adderall.game.framework.component.GameComponent;
import team.adderall.game.framework.component.Name;

@GameConfiguration
public class GameComponents {
    public final static int NUM_OF_GAME_COMPONENTS = 5;

    private boolean randomGameLogicExecuted = false;
    private int gameLogicWithParamExecuted1 = 0;
    private String gameLogicWithParamExecuted2 = "";

    public void testingReflection() {}

    @GameComponent
    public int getASixer() {
        return 6;
    }
    @GameComponent
    public String getAnders() {
        return "anders";
    }
    @GameComponent
    public Logicer randomGameLogic(){
        return () -> randomGameLogicExecuted = true;
    }
    @GameComponent
    public Logicer gameLogicWithParams(@Name("getASixer") final int v, @Name("getAnders") final String n){
        return () -> {
            gameLogicWithParamExecuted1 = v;
            gameLogicWithParamExecuted2 = n;
        };
    }
    @GameComponent
    public Object testPlease(@Name("gameLogicWithParams") Logicer t) {
        assert(this.gameLogicWithParamExecuted2.equals(""));
        t.run();
        assert(this.gameLogicWithParamExecuted2.equals(this.getAnders()));

        return "test";
    }
}
