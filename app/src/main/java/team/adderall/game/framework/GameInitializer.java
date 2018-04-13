package team.adderall.game.framework;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import team.adderall.GameActivity;

public class GameInitializer {

    private final GameContext ctx;
    private final List<Class<?>> configClasses;
    private GameConfigurationLoader configLoader;

    /**
     * A package to be scanned for classes with the annotation @GameConfiguration
     *
     * @param configClasses example: team.adderall.game.Configuration.class
     */
    public GameInitializer(final Class<?>... configClasses) {
        this.ctx = new GameContext();

        this.configClasses = new ArrayList<>();
        this.configClasses.addAll(Arrays.asList(configClasses));
    }

    /**
     * Crash the application
     *
     * @param err
     */
    private void missingRequiredInstance(final String err) {
        throw new InstantiationError("a required class instance is missing and must be registered: " + err);
    }

    /**
     * Load all instances to memory
     */
    private void load() {
        this.configLoader = new GameConfigurationLoader(this.ctx, this.configClasses);

        // load all @GameComponents from @GameConfiguration classes
        this.configLoader.activateFailOnNullInstance();
        this.configLoader.load();
    }

    /**
     * Load all instances to memory and notify listener
     */
    public void load(final GameFinishedLoading callback) {
        final GameInitializer self = this;
        (new Thread(() -> {
            self.load();

            if (callback == null) {
                return;
            }
            callback.trigger();
        })).start();
    }

    /**
     * Start the game loop in a new thread
     */
    public void start() {
        System.out.print("OK");
    }

    public void loadEssentials() {
        this.configClasses.add(EssentialGameConfigurationDependencies.class);
    }

    public void addGameConfigurationInstances(Object... instances) {

    }
}
