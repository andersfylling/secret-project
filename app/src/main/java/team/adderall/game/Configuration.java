package team.adderall.game;

import team.adderall.game.framework.component.GameComponent;
import team.adderall.game.framework.GameConfiguration;
import team.adderall.game.framework.GameContext;
import team.adderall.game.framework.GameContextGetterAssured;
import team.adderall.game.framework.GameContextSetter;
import team.adderall.game.framework.Logicer;

/**
 * Reference this class in the GameActivity when initializing the game.
 */
@GameConfiguration
public class Configuration
{
    @GameComponent
    public void registerInstances(GameContextGetterAssured ctx, GameContextSetter setter) {
        LogicManager logic = (LogicManager) ctx.getAssuredInstance(GameContext.LOGIC);
        PaintManager painter = (PaintManager) ctx.getAssuredInstance(GameContext.PAINT);

        // register a new game instance
        setter.setInstance("Game", new Game(logic, painter));
    }

    @GameComponent
    public Logicer[][] setLogicManager(final GameContextGetterAssured ctx) {
        // same as GPU logic, a wave can hold N task which can run in parallel
        // but each wave is sequential

        Logicer[] firstWave = new Logicer[]{
            new LogicManager()
        };

        // group waves
        return new Logicer[][]{
                firstWave
        };
    }
}
