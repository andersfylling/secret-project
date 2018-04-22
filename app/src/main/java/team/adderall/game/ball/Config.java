package team.adderall.game.ball;

import team.adderall.game.framework.component.GameComponents;
import team.adderall.game.framework.configuration.GameConfiguration;

@GameConfiguration
@GameComponents({
        DrawBall.class,
        LogicBall.class,
        BallManager.class
})
public class Config {}
