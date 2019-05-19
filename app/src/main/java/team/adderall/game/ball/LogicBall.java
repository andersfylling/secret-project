package team.adderall.game.ball;

import addy.annotations.*;
import team.adderall.game.gameloop.GameLogicInterface;


@Service
public class LogicBall implements GameLogicInterface{
    private BallManager ball;

    @DepWire
    public LogicBall(@Inject("ballManager") BallManager ball)
    {
        this.ball = ball;
    }

    @Override
    public void run() {
        //ball.doLogic(); - Is there any logic? as Gravity and Colission is handled, and we have a seperate class for
        //sensorevent.
    }
}
