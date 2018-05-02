package team.adderall.game.framework;

import org.junit.Test;


import team.adderall.game.framework.configuration.GameConfigurationLoader;
import team.adderall.game.framework.gameconfigs.GameComponentsWithDepCycling;
import team.adderall.game.framework.gameconfigs.GameComponentsWithSelfDepCycling;

public class GameComponentTest {
    public GameComponentTest() {}

    @Test
    public void testLoadSimpleGameComponent() {
        GameInitializer initializer = new GameInitializer(
                team.adderall.game.framework.gameconfigs.GameComponents.class
        );
        initializer.load(() -> {});
    }



    // Check for cycling dependency issues
    //

    @Test(expected = InstantiationError.class)
    public void testSelfCyclingDependency() {
        GameConfigurationLoader loader = new GameConfigurationLoader(GameComponentsWithSelfDepCycling.class);
        loader.load();
    }

    @Test(expected = InstantiationError.class)
    public void testCyclingDependency() {
        GameConfigurationLoader loader = new GameConfigurationLoader(GameComponentsWithDepCycling.class);
        loader.load();
    }
}
