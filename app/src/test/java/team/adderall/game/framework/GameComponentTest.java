package team.adderall.game.framework;

import org.junit.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import team.adderall.game.framework.component.GameComponentData;
import team.adderall.game.framework.component.GameComponent;
import team.adderall.game.framework.gameconfigs.GameComponents;
import team.adderall.game.framework.gameconfigs.GameComponentsWithDepCycling;
import team.adderall.game.framework.gameconfigs.GameComponentsWithSelfDepCycling;

public class GameComponentTest {
    public GameComponentTest() {}

    @Test
    public void testLoadSimpleGameComponent() {
        List<GameComponentData> components = new ArrayList<>();

        Object instance = null;
        try {
            instance = Class.forName(GameComponents.class.getName()).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // get components
        for (Method method : GameComponents.class.getDeclaredMethods()) {
            // ensure method is a RegisterGameComponent
            GameComponent component = method.getAnnotation(GameComponent.class);
            if (component == null) {
                continue;
            }

            // configure component
            // if the name attribute is not set, use the method name as fallback
            String componentName = component.name();
            if (componentName.isEmpty()) {
                componentName = method.getName();
            }

            GameComponentData data = new GameComponentData(componentName, method, instance); //componentName, method.getParameters(), method.getReturnType());
            data.checkForSelfDependencyCyclingIssues();
            components.add(data);
        }
        assert(components.size() > 0);

        // sort based on dependencies: low => high
        Collections.sort(components);

        // instantiate components
        for (GameComponentData component : components) {
            component.initiate(components);
        }

        "".isEmpty();
    }



    // Check for cycling dependency issues
    //

    @Test(expected = InstantiationError.class)
    public void testSelfCyclingDependency() {
        List<GameComponentData> components = new ArrayList<>();

        // get components
        for (Method method : GameComponentsWithSelfDepCycling.class.getDeclaredMethods()) {
            // ensure method is a RegisterGameComponent
            GameComponent component = method.getAnnotation(GameComponent.class);
            if (component == null) {
                continue;
            }

            // configure component
            // if the name attribute is not set, use the method name as fallback
            String componentName = component.name();
            if (componentName.isEmpty()) {
                componentName = method.getName();
            }

            GameComponentData data = new GameComponentData(componentName, method, this);
            data.checkForSelfDependencyCyclingIssues();
            components.add(data);
        }
        // this checks for cycling dep
        Collections.sort(components);
    }

    @Test(expected = InstantiationError.class)
    public void testCyclingDependency() {
        List<GameComponentData> components = new ArrayList<>();

        // get components
        for (Method method : GameComponentsWithDepCycling.class.getDeclaredMethods()) {
            // ensure method is a RegisterGameComponent
            GameComponent component = method.getAnnotation(GameComponent.class);
            if (component == null) {
                continue;
            }

            // configure component
            // if the name attribute is not set, use the method name as fallback
            String componentName = component.name();
            if (componentName.isEmpty()) {
                componentName = method.getName();
            }

            GameComponentData data = new GameComponentData(componentName, method, this);
            data.checkForSelfDependencyCyclingIssues();
            components.add(data);
        }
        // this checks for cycling dep
        Collections.sort(components);
    }
}
