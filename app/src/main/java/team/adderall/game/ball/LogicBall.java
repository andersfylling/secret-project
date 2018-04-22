package team.adderall.game.ball;

import team.adderall.game.framework.GameLogicInterface;
import team.adderall.game.framework.component.GameComponent;
import team.adderall.game.framework.component.GameDepWire;
import team.adderall.game.framework.component.Inject;

@GameComponent
public class LogicBall implements GameLogicInterface{
    BallManager ball;

    @GameDepWire
    LogicBall(@Inject("ballManager") BallManager ball)
    {
        this.ball = ball;
    }

    @Override
    public void run() {
        //ball.doLogic(); - Is there any logic? as Gravity and Colission is handled, and we have a seperate class for
        //sensorevent.
    }
}
