package team.adderall.game.framework;

import android.util.Log;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import team.adderall.game.framework.component.GameComponent;
import team.adderall.game.framework.component.GameComponentData;

public class GameConfigurationLoader
{
    private static final String TAG = "GameConfigurationLoader";

    private final List<Class<?>> configs;
    private final List<GameComponentData> components;
    private final GameContextSetter ctx;

    private boolean failOnNullInstance;

    public GameConfigurationLoader(final GameContextSetter ctx, final List<Class<?>> configs) {
        this.configs = new ArrayList<>(configs);
        this.components = new ArrayList<>();
        this.ctx = ctx;
        this.failOnNullInstance = false;
    }

    public GameConfigurationLoader(GameContext ctx, Class<?>... configs) {
        this.configs = new ArrayList<>();
        this.configs.addAll(Arrays.asList(configs));

        this.components = new ArrayList<>();
        this.ctx = ctx;
        this.failOnNullInstance = false;
    }

    private void loadGameComponentRegisters(final Class<?> config) {

        Object instance = null;
        try {
            instance = Class.forName(config.getName()).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (instance == null) {
            return;
        }

        for (Method method : config.getDeclaredMethods()) {
            // ensure method is a GameComponent
            GameComponent component = method.getAnnotation(GameComponent.class);
            if (component == null) {
                continue;
            }

            // configure component
            // if the name attribute is not set, use the method name as fallback
            String name = "";
            String componentName = component.name();
            String componentVal = component.value();
            if (componentVal.isEmpty() && componentName.isEmpty()) {
                name = method.getName();
            } else if (componentVal.isEmpty() && !componentName.isEmpty()) {
                name = componentName;
            } else if (!componentVal.isEmpty() && componentName.isEmpty()) {
                name = componentVal;
            } else if (!componentVal.isEmpty() && !componentName.isEmpty() && componentName.equals(componentVal)) {
                name = componentName;
            } else if (!componentVal.isEmpty() && !componentName.isEmpty() && !componentName.equals(componentVal)) {
                throw new InstantiationError("different names suggested for game component when only one or zero is expected: " + componentName + ", " + componentVal);
            }

            GameComponentData data = new GameComponentData(name, method, instance);
            data.checkForSelfDependencyCyclingIssues();
            components.add(data);
        }
    }

    /**
     * Crash the program if a null instance is detected.
     * Otherwise a warning is given.
     */
    public void activateFailOnNullInstance() {
        this.failOnNullInstance = true;
    }

    public void load() {
        for (final Class<?> config : this.configs) {
            // ignore classes that aren't marked GameConfiguration
            Annotation valid = config.getAnnotation(GameConfiguration.class);
            if (valid == null) {
                continue;
            }

            this.loadGameComponentRegisters(config);
        }

        // add GameContext
        this.components.add(new GameComponentData(GameContext.NAME, this.ctx));

        // sort based on dependencies
        Collections.sort(this.components);

        // instantiate components
        for (GameComponentData component : this.components) {
            component.initiate(this.components);
        }

        // check for null instances and give a warning or fail
        for (GameComponentData component : this.components) {
            if (component.getInstance() != null) {
                continue;
            }

            String err = "instance for game component was null: " + component.getName();
            if (this.failOnNullInstance) {
                throw new InstantiationError(err);
            } else {
                Log.e(TAG, err);
            }
        }

        // TODO: check for name duplicates

        // add instances to game context
        for (GameComponentData component : this.components) {
            this.ctx.setInstance(component.getName(), component.getInstance());
        }

        // clear Game Components from this instance to free up memory
        this.components.clear();
    }
}
