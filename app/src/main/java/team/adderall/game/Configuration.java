package team.adderall.game;

import team.adderall.game.framework.GamePainter;
import team.adderall.game.framework.component.GameComponent;
import team.adderall.game.framework.configuration.GameConfiguration;
import team.adderall.game.framework.context.GameContext;
import team.adderall.game.framework.context.GameContextGetterAssured;
import team.adderall.game.framework.context.GameContextSetter;
import team.adderall.game.framework.GameLogicInterface;
import team.adderall.game.framework.component.GameComponentRegister;
import team.adderall.game.framework.component.Inject;

/**
 * Reference this class in the GameActivity when initializing the game.
 */
@GameConfiguration
public class Configuration
{
    @GameComponentRegister // TODO: implement logic. not currently working.
    public void registerInstances(@Inject(GameContext.NAME) GameContextGetterAssured ctx, @Inject("GameContextSetter") GameContextSetter setter) {
        LogicManager logic = (LogicManager) ctx.getAssuredInstance(GameContext.LOGIC);
        PaintManager painter = (PaintManager) ctx.getAssuredInstance(GameContext.PAINT);

        // register a new game instance
        setter.setInstance("Game", new Game(logic, painter));
    }

    @GameComponent(GameContext.LOGIC)
    public GameLogicInterface[][] setLogicWaves(@Inject(GameContext.NAME) GameContextGetterAssured ctx) {
        // same as GPU logic, a wave can hold N task which can run in parallel
        // but each wave is sequential

        GameLogicInterface[] firstWave = new GameLogicInterface[]{
            new LogicManager()
        };

        // group waves
        return new GameLogicInterface[][]{
                firstWave
        };
    }

    @GameComponent(GameContext.PAINT)
    public GamePainter[][] setPaintWaves(@Inject(GameContext.NAME) GameContextGetterAssured ctx) {
        // same as GPU logic, a wave can hold N task which can run in parallel
        // but each wave is sequential

        GamePainter[] firstWave = new GamePainter[]{
            new PaintManager()
        };

        // group waves
        return new GamePainter[][]{
                firstWave
        };
    }
}
