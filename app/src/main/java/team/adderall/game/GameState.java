package team.adderall.game;

import team.adderall.game.Players;
import team.adderall.game.ball.BallManager;
import team.adderall.game.framework.component.GameComponent;
import team.adderall.game.framework.component.GameDepWire;
import team.adderall.game.framework.component.Inject;

@GameComponent("GameState")
public class GameState
{
    private float yScaleValue=0;

    @GameDepWire
    public GameState() {
    }

    public void setyScaleValue(float y){
        this.yScaleValue = y;
    }

    public float getyScaleValue(){
        return yScaleValue;
    }
}
