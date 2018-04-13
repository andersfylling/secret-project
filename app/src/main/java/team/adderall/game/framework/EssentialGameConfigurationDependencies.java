package team.adderall.game.framework;


import team.adderall.game.framework.component.GameComponent;
import team.adderall.game.framework.component.Name;

@GameConfiguration
public class EssentialGameConfigurationDependencies
{
    private final static String GAME_CONTEXT = GameContext.NAME;

    @GameComponent("GameLoop")
    public GameLoop gameLoop(@Name(GameContext.LOGIC) Logicer[][] logics, @Name(GameContext.PAINT) Painter[][] painters) {
        return new GameLoop(logics, painters);
    }

}
