package team.adderall.game;

import team.adderall.game.framework.Painter;
import team.adderall.game.framework.component.GameComponent;
import team.adderall.game.framework.GameConfiguration;
import team.adderall.game.framework.GameContext;
import team.adderall.game.framework.GameContextGetterAssured;
import team.adderall.game.framework.GameContextSetter;
import team.adderall.game.framework.Logicer;
import team.adderall.game.framework.component.GameComponentRegister;
import team.adderall.game.framework.component.Name;

/**
 * Reference this class in the GameActivity when initializing the game.
 */
@GameConfiguration
public class Configuration
{
    @GameComponentRegister // TODO: implement logic. not currently working.
    public void registerInstances(@Name(GameContext.NAME) GameContextGetterAssured ctx, @Name("GameContextSetter") GameContextSetter setter) {
        LogicManager logic = (LogicManager) ctx.getAssuredInstance(GameContext.LOGIC);
        PaintManager painter = (PaintManager) ctx.getAssuredInstance(GameContext.PAINT);

        // register a new game instance
        setter.setInstance("Game", new Game(logic, painter));
    }

    @GameComponent(GameContext.LOGIC)
    public Logicer[][] setLogicWaves(@Name(GameContext.NAME) GameContextGetterAssured ctx) {
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

    @GameComponent(GameContext.PAINT)
    public Painter[][] setPaintWaves(@Name(GameContext.NAME) GameContextGetterAssured ctx) {
        // same as GPU logic, a wave can hold N task which can run in parallel
        // but each wave is sequential

        Painter[] firstWave = new Painter[]{
            new PaintManager()
        };

        // group waves
        return new Painter[][]{
                firstWave
        };
    }
}
