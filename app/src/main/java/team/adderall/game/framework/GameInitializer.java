package team.adderall.game.framework;

import android.app.Activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import team.adderall.game.framework.configuration.EssentialGameConfigurationDependencies;
import team.adderall.game.framework.configuration.GameConfigurationLoader;
import team.adderall.game.framework.context.GameContext;

public class GameInitializer
{

    private final GameContext ctx;
    private final List<Class<?>> configClasses;
    private final List<Activity> configClassInstances;
    private final GameConfigurationLoader configLoader;

    /**
     * A package to be scanned for classes with the annotation @GameConfiguration
     *
     * @param configClasses example: team.adderall.game.Configuration.class
     */
    public GameInitializer(final Class<?>... configClasses) {
        this.ctx = new GameContext();

        this.configClassInstances = new ArrayList<>();
        this.configClasses = new ArrayList<>();
        this.configClasses.addAll(Arrays.asList(configClasses));

        this.configLoader = new GameConfigurationLoader(this.configClasses);
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
        this.configLoader.addGameConfigurationInstances(this.configClassInstances);

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
            configLoader.installGameComponents(ctx);
            configLoader.findGameDepWireMethodsAndPopulate(); // inject GameDepWire methods

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
        Runnable gameLoop = (Runnable) this.ctx.getAssuredInstance("GameLoop");
        (new Thread(gameLoop)).start();

        System.out.println("\nRUNNING\n");
    }

    public void loadEssentials() {
        this.configLoader.addGameConfigurations(EssentialGameConfigurationDependencies.class);
        this.configLoader.addGameComponentInstance(GameContext.NAME, this.ctx);
    }

    /**
     * Must be added before this.load is called.
     *
     * @param instances
     */
    public void addGameConfigurationActivities(Activity... instances) {
        this.configClassInstances.addAll(Arrays.asList(instances));
    }
}
