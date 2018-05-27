package team.adderall.game.ball;

import addy.annotations.*;

@Configuration
@ServiceLinker({
        DrawBall.class,
        LogicBall.class,
        BallManager.class
})
public class Config {}
