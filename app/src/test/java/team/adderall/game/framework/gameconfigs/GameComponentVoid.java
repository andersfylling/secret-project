package team.adderall.game.framework.gameconfigs;

import team.adderall.game.framework.GameConfiguration;
import team.adderall.game.framework.Logicer;
import team.adderall.game.framework.component.GameComponent;
import team.adderall.game.framework.component.GameComponentRegister;
import team.adderall.game.framework.component.Inject;

import static org.junit.Assert.assertEquals;

@GameConfiguration
public class GameComponentVoid {
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
    @GameComponentRegister
    public void gameLogicWithParams(@Inject("getASixer") final int v, @Inject("getAnders") final String n){
        return;
    }
}
