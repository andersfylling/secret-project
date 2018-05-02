package team.adderall.game.framework;

import android.app.Activity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import team.adderall.game.framework.component.GameLogic;
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

            // load all the components into memory and initialize them
            self.load();

            // add the component instances to the GameContext
            configLoader.installGameComponents(ctx);

            // extract GameLogic components and add them to the GameLogicManager
            self.populateGameLogicManager();

            // inject GameDepWire methods
            configLoader.findGameDepWireMethodsAndPopulate();


            // check if we should fire the callback
            if (callback != null) {
                callback.trigger();
            }
        })).start();
    }

    /**
     * Start the game loop in a new thread
     */
    public void start() {
        Runnable gameLoop = (Runnable) this.ctx.getAssuredInstance("GameLoop");
        (new Thread(gameLoop)).start();
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

    private void populateGameLogicManager() {
        GameLogicManager manager = (GameLogicManager) this.ctx.getAssuredInstance("gameLogicManager");

        for (Map.Entry<String, Object> entry : this.ctx.getInstances().entrySet()) {
            Object component = entry.getValue();
            if (component.getClass().getAnnotation(GameLogic.class) == null) {
                continue;
            }

            int wave = component.getClass().getAnnotation(GameLogic.class).wave();
            manager.addGameLogic(wave, (GameLogicInterface) component);
        }
    }

    /**
     * When shutting down the game session, go through every game component and run
     * every method with the name close.
     */
    public void close() {
        for (Map.Entry<String, Object> entry : this.ctx.getInstances().entrySet()) {
            Object component = entry.getValue();
            Method[] methods = component.getClass().getMethods();
            for (Method method : methods) {
                if (method.getName().equals("close") && method.getParameterTypes().length == 0) {
                    try {
                        method.invoke(component);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
