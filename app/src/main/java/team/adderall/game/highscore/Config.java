package team.adderall.game.highscore;

import team.adderall.game.GameState;
import team.adderall.game.Players;
import team.adderall.game.framework.component.GameComponent;
import team.adderall.game.framework.component.Inject;
import team.adderall.game.framework.configuration.GameConfiguration;

@GameConfiguration
public class Config {
    @GameComponent("DrawHighScore")
    public DrawHighScore highScore(
            @Inject("players") Players players,
            @Inject("GameState") GameState gameState
    ) {
        return new DrawHighScore(players,gameState);
    }
}
