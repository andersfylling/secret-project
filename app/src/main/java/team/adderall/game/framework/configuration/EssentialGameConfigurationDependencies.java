package team.adderall.game.framework.configuration;


import team.adderall.game.framework.GameLogicManager;
import team.adderall.game.framework.GameLoop;
import team.adderall.game.framework.GameLogicInterface;
import team.adderall.game.framework.GamePaintWrapper;
import team.adderall.game.framework.GamePainter;
import team.adderall.game.framework.component.GameComponent;
import team.adderall.game.framework.component.GameComponents;
import team.adderall.game.framework.component.Inject;
import team.adderall.game.framework.context.GameContext;

@GameConfiguration
@GameComponents({
        GameLoop.class,
        GameLogicManager.class
})
public class EssentialGameConfigurationDependencies
{
    private final static String GAME_CONTEXT = GameContext.NAME;
}
