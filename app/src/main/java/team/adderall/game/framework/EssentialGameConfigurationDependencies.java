package team.adderall.game.framework;


import team.adderall.game.framework.component.GameComponent;
import team.adderall.game.framework.component.Inject;

@GameConfiguration
public class EssentialGameConfigurationDependencies
{
    private final static String GAME_CONTEXT = GameContext.NAME;

    @GameComponent("GameLoop")
    public GameLoop gameLoop(@Inject(GameContext.LOGIC) Logicer[][] logics, @Inject(GameContext.PAINT) Painter[][] painters) {
        return new GameLoop(logics, painters);
    }

}
