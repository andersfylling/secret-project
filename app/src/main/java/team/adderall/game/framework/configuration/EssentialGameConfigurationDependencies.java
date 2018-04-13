package team.adderall.game.framework.configuration;


import team.adderall.game.framework.GameLoop;
import team.adderall.game.framework.GameLogicInterface;
import team.adderall.game.framework.GamePainter;
import team.adderall.game.framework.component.GameComponent;
import team.adderall.game.framework.component.Inject;
import team.adderall.game.framework.context.GameContext;

@GameConfiguration
public class EssentialGameConfigurationDependencies
{
    private final static String GAME_CONTEXT = GameContext.NAME;

    @GameComponent("GameLoop")
    public GameLoop gameLoop(@Inject(GameContext.LOGIC) GameLogicInterface[][] logics, @Inject(GameContext.PAINT) GamePainter[][] painters) {
        return new GameLoop(logics, painters);
    }

}
