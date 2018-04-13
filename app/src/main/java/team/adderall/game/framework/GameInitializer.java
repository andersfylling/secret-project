package team.adderall.game.framework;

import java.lang.reflect.Method;

public class GameInitializer {

    private final GameContext ctx;
    private final Class<?> configClass;

    /**
     * A package to be scanned for classes with the annotation @GameConfiguration
     *
     * @param configurationPackageName example: "team.adderall.game"
     */
    public GameInitializer(final Class<?> configClass) {
        this.ctx = new GameContext();
        this.configClass = configClass;
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
//        // register all methods with @Bean annotation
//        for (Method method : this.configClass.getDeclaredMethods()) {
//            String name = method.getName();
//
//            MyAnnotation a = m.getAnnotation(MyAnnotation.class);
//            MyValueType value1 = a.attribute1();
//        }
//
//        // register logic manager to be run in game loop
//        Object logic = config.setLogicManager();
//        if (logic == null) {
//            logic = config.setLogicManager(this.ctx);
//            if (logic == null) {
//                this.missingRequiredInstance(GameContext.LOGIC);
//            }
//        }
//        this.ctx.setInstance(GameContext.LOGIC, logic);
//
//        // register paint manager to be run every N time per second
//        Object painter = config.setPaintManager();
//        if (painter == null) {
//            painter = config.setPaintManager(this.ctx);
//            if (painter == null) {
//                this.missingRequiredInstance(GameContext.PAINT);
//            }
//        }
//        this.ctx.setInstance(GameContext.PAINT, painter);
//
//        // register sensor event handler, this is optional
//        if (this.ctx.getInstance(GameContext.SENSOR_EVENT) == null) {
//            return;
//        }
//        Object sensors = config.setSensorEventManager();
//        if (sensors == null) {
//            sensors = config.setSensorEventManager(this.ctx);
//        }
//        this.ctx.setInstance(GameContext.SENSOR_EVENT, sensors);
    }

    /**
     * Load all instances to memory and notify listener
     */
    public void load(final GameFinishedLoading callback) {
        this.load();

        if (callback == null) {
            return;
        }
        callback.trigger();
    }

    /**
     * Start the game loop in a new thread
     */
    public void start() {

    }
}
