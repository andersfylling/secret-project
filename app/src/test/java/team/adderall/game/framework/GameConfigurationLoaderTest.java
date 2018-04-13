package team.adderall.game.framework;

import org.junit.Test;

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
        GameContext ctx = new GameContext();
        GameConfigurationLoader loader = new GameConfigurationLoader(ctx, c);
        loader.activateFailOnNullInstance();
        loader.load();

        // make sure all the methods were found, processed and instantiated
        assertEquals(GameComponents.NUM_OF_GAME_COMPONENTS, ctx.sizeWithoutGameContext());
    }

    @Test
    public void testLoadingClassWithoutGameConfiguration() {
        Class<?> c = GameComponentsWithoutGameConfigAnnotation.class;
        GameContext ctx = new GameContext();
        GameConfigurationLoader loader = new GameConfigurationLoader(ctx, c);
        loader.activateFailOnNullInstance();
        loader.load();

        // make sure all the methods were found, processed and instantiated
        assertEquals(0, ctx.sizeWithoutGameContext());
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
        assertEquals(GameComponents.NUM_OF_GAME_COMPONENTS + GameComponentsExtra.NUM_OF_GAME_COMPONENTS, ctx.sizeWithoutGameContext());
    }

    @Test(expected = InstantiationError.class)
    public void testGameComponentWithUnknownDependency() {
        Class<?> c = GameComponentsWithUnknownDep.class;
        GameContext ctx = new GameContext();
        GameConfigurationLoader loader = new GameConfigurationLoader(ctx, c);
        loader.activateFailOnNullInstance();
        loader.load();

        assertEquals(0, ctx.size());
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
