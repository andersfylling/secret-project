package team.adderall.game.framework;

import android.graphics.Canvas;

import org.junit.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import team.adderall.game.framework.component.GameComponentData;
import team.adderall.game.framework.component.Name;
import team.adderall.game.framework.component.GameComponent;

public class GameComponentTest {
    public GameComponentTest() {}

    private boolean randomGameLogicExecuted = false;
    private int gameLogicWithParamExecuted1 = 0;
    private String gameLogicWithParamExecuted2 = "";

    public void testingReflection() {}

    @GameComponent
    public int getASixer() {
        return 6;
    }
    @GameComponent
    public String getAnders() {
        return "anders";
    }
    @GameComponent
    public Logicer randomGameLogic(){
        return () -> randomGameLogicExecuted = true;
    }
    @GameComponent
    public Logicer gameLogicWithParams(@Name("getASixer") final int v, @Name("getAnders") final String n){
        return () -> {
            gameLogicWithParamExecuted1 = v;
            gameLogicWithParamExecuted2 = n;
        };
    }
    @GameComponent
    public Object testPlease(@Name("gameLogicWithParams") Logicer t) {
        assert(this.gameLogicWithParamExecuted2.equals(""));
        t.run();
        assert(this.gameLogicWithParamExecuted2.equals(this.getAnders()));

        return null;
    }

    @Test
    public void testLoadSimpleGameComponent() {
        List<GameComponentData> components = new ArrayList<>();

        // get components
        for (Method method : this.getClass().getDeclaredMethods()) {
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

            GameComponentData data = new GameComponentData(componentName, method, this); //componentName, method.getParameters(), method.getReturnType());
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
