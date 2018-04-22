package team.adderall.game.framework;

import org.junit.Test;

import team.adderall.game.framework.configuration.GameConfigurationLoader;
import team.adderall.game.framework.context.GameContext;
import team.adderall.game.framework.gameconfigs.GameComponents;
import team.adderall.game.framework.gameconfigs.GameComponentsExtra;
import team.adderall.game.framework.gameconfigs.GameComponentsWithDepCycling;
import team.adderall.game.framework.gameconfigs.GameComponentsWithUnknownDep;
import team.adderall.game.framework.gameconfigs.GameComponentsWithoutGameConfigAnnotation;
import team.adderall.game.framework.gameconfigs.GameComponentsWithSelfDepCycling;

import static org.junit.Assert.assertEquals;

public class GameConfigurationLoaderTest {
    @Test
    public void testLoadingGameConfiguration() {
        Class<?> c = GameComponents.class;
        GameConfigurationLoader loader = new GameConfigurationLoader(c);
        loader.activateFailOnNullInstance();
        loader.load();

        GameContext ctx = new GameContext();
        loader.installGameComponents(ctx);

        // make sure all the methods were found, processed and instantiated
        assertEquals(GameComponents.NUM_OF_GAME_COMPONENTS, ctx.size());
    }

    @Test
    public void testLoadingClassWithoutGameConfiguration() {
        Class<?> c = GameComponentsWithoutGameConfigAnnotation.class;
        GameContext ctx = new GameContext();
        GameConfigurationLoader loader = new GameConfigurationLoader(c);
        loader.activateFailOnNullInstance();
        loader.load();
        loader.installGameComponents(ctx);

        // make sure all the methods were found, processed and instantiated
        assertEquals(0, ctx.size());
    }

    @Test
    public void testLoadingMultipleGameConfigurations() {
        Class<?> c1 = GameComponents.class;
        Class<?> c2 = GameComponentsExtra.class;
        GameConfigurationLoader loader = new GameConfigurationLoader(c1, c2);
        loader.activateFailOnNullInstance();
        loader.load();

        GameContext ctx = new GameContext();
        loader.installGameComponents(ctx);

        // make sure all the methods were found, processed and instantiated
        assertEquals(GameComponents.NUM_OF_GAME_COMPONENTS + GameComponentsExtra.NUM_OF_GAME_COMPONENTS, ctx.size());
    }

    @Test(expected = InstantiationError.class)
    public void testGameComponentWithUnknownDependency() {
        Class<?> c = GameComponentsWithUnknownDep.class;
        GameConfigurationLoader loader = new GameConfigurationLoader(c);
        loader.activateFailOnNullInstance();
        loader.load();

        GameContext ctx = new GameContext();
        loader.installGameComponents(ctx);

        assertEquals(0, ctx.size());
    }

    @Test(expected = InstantiationError.class)
    public void testSelfCyclingDependency() {
        Class<?> c = GameComponentsWithSelfDepCycling.class;
        GameConfigurationLoader loader = new GameConfigurationLoader(c);
        loader.activateFailOnNullInstance();
        loader.load();
    }

    @Test(expected = InstantiationError.class)
    public void testCyclingDependency() {
        Class<?> c = GameComponentsWithDepCycling.class;
        GameConfigurationLoader loader = new GameConfigurationLoader(c);
        loader.activateFailOnNullInstance();
        loader.load();
    }

}
