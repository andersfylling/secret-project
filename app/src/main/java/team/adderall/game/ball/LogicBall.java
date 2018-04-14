package team.adderall.game.ball;

import team.adderall.game.framework.GameLogicInterface;
import team.adderall.game.framework.component.GameComponent;
import team.adderall.game.framework.component.GameDepWire;

/**
 * Created by Cim on 14/4/18.
 */
@GameComponent("logicBall")
public class LogicBall implements GameLogicInterface{
    BallManager ball;

    @GameDepWire
    LogicBall(BallManager ball){
        this.ball = ball;
    }
    @Override
    public void run() {
        //ball.doLogic(); - Is there any logic? as Gravity and Colission is handled, and we have a seperate class for
        //sensorevent.
    }
}
