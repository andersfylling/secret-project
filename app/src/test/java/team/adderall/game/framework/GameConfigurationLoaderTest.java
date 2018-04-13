package team.adderall.game.framework;

import org.junit.Test;

import team.adderall.game.framework.gameconfigs.GameComponents;
import team.adderall.game.framework.gameconfigs.GameComponentsExtra;
import team.adderall.game.framework.gameconfigs.GameComponentsWithDepCycling;
import team.adderall.game.framework.gameconfigs.GameComponentsWithSelfDepCycling;

public class GameConfigurationLoaderTest {
    @Test
    public void testLoadingGameConfiguration() {
        Class<?> c = GameComponents.class;
        GameContext ctx = new GameContext();
        GameConfigurationLoader loader = new GameConfigurationLoader(ctx, c);
        loader.activateFailOnNullInstance();
        loader.load();

        // make sure all the methods were found, processed and instantiated
        assert(ctx.size() == c.getMethods().length);
    }

    @Test
    public void testLoadingMultipleGameConfigurations() {
        Class<?> c1 = GameComponents.class;
        Class<?> c2 = GameComponentsExtra.class;
        GameContext ctx = new GameContext();
        GameConfigurationLoader loader = new GameConfigurationLoader(ctx, c1, c2);
        loader.activateFailOnNullInstance();
        loader.load();

        // make sure all the methods were found, processed and instantiated
        assert(ctx.size() == c1.getMethods().length + c2.getMethods().length);
    }

    @Test(expected = InstantiationError.class)
    public void testSelfCyclingDependency() {
        Class<?> c = GameComponentsWithSelfDepCycling.class;
        GameContext ctx = new GameContext();
        GameConfigurationLoader loader = new GameConfigurationLoader(ctx, c);
        loader.activateFailOnNullInstance();
        loader.load();
    }

    @Test(expected = InstantiationError.class)
    public void testCyclingDependency() {
        Class<?> c = GameComponentsWithDepCycling.class;
        GameContext ctx = new GameContext();
        GameConfigurationLoader loader = new GameConfigurationLoader(ctx, c);
        loader.activateFailOnNullInstance();
        loader.load();
    }

}
