package team.adderall.game.framework.component;

import android.support.annotation.NonNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import team.adderall.game.framework.context.GameContext;
import team.adderall.game.framework.context.GameContextSetter;

public class GameComponentData
        implements Comparable<GameComponentData>
{
    private final String name;
    private final Annotation[][] params;
    private final String[] paramsName;
    private final Class<?> type;
    private final Method method;
    private final Object classInstance;

    private Object instance;

    public GameComponentData(final String name, Method method, Object classInstance) {
        this.name = name;
        this.method = method;
        this.params = method.getParameterAnnotations();
        this.type = method.getReturnType();
        this.classInstance = classInstance;

        this.paramsName = new String[this.params.length];
        int counter = 0;
        for (Annotation[] annotations : this.params) {
            if (annotations.length == 0) {
                continue;
            }

            for (Annotation a : annotations) {
                this.paramsName[counter++] = this.getNameAnnotationValue(a);
            }
        }

        this.instance = null;
    }

    public GameComponentData(String name, GameContextSetter ctx) {
        this.name = name;
        this.instance = ctx;
        this.params = new Annotation[][]{};
        this.paramsName = new String[]{};
        this.type = GameContext.class;
        this.method = null;
        this.classInstance = null;
    }

    public Annotation[][] getParams() {
        return params;
    }

    public String[] getParamsName() {
        return this.paramsName;
    }

    public String getName() {
        return name;
    }

    public Object getInstance() {
        return instance;
    }

    public void setInstance(Object instance) {
        this.instance = instance;
    }

    public Class<?> getType() {
        return type;
    }

    private String getNameAnnotationValue(Annotation a) {
        String val = "";
        if (a.annotationType() == Inject.class) {
            Class<? extends Annotation> type = a.annotationType();

            for (Method m : type.getDeclaredMethods()) {
                Object value = null;
                try {
                    value = m.invoke(a, (Object[])null);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }

                if (m.getName().equals("value")) {
                    val = (String) value;
                    break;
                }
            }
        }

        return val;
    }

    public void checkForSelfDependencyCyclingIssues() {
        // check if this component uses itself as a dependency
        String errorOnParamName = "cannot depend on itself before being created (cycling dependency): ";
        for (String param : this.paramsName) {
            if (param.equals(this.getName())) {
                throw new InstantiationError(errorOnParamName + this.getName());
            }
        }
    }

    public boolean dependsOnComponent(String component) {
        for (String param : this.paramsName) {
            if (param.equals(component)) {
                return true;
            }
        }

        return false;
    }

    public void initiate(List<GameComponentData> components) {
        if (this.instance != null) {
            return;
        }
        components = new ArrayList<>(components);

        Object[] dependencies = new Object[this.params.length];

        int counter = 0;
        for (String param : this.paramsName) {
            // find
            for (GameComponentData component : components) {
                if (param.equals(component.getName())) {
                    dependencies[counter] = component.getInstance();

                    // check if instance haven't been initiated
                    if (component.getInstance() == null) {
                        throw new InstantiationError("game component was expected to be instantiated: " + component.getName());
                    }

                    counter++;
                    break;
                }
            }
        }

        // check if any dependencies are missing
        if (dependencies.length > 0 && dependencies[dependencies.length - 1] == null) {
            String err = "missing dependencies for game component: " + this.getName();


            err += ": params" + this.getParamsAsString();

            throw new InstantiationError(err);
        }

        // assumption: everything ok
        try {
            if (this.paramsName.length == 0) {
                this.instance = this.method.invoke(this.classInstance);
            } else {
                this.instance = this.method.invoke(this.classInstance, dependencies);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }


    public String getParamsAsString() {
        String params = "{";
        for (String param : this.paramsName) {
            params += param + ",";
        }
        params += "}";

        return params;
    }

    @Override
    public int compareTo(@NonNull GameComponentData gameComponentData) {
        System.out.print("comparing " + this.getName() + this.getParamsAsString() + " to " + gameComponentData.getName() + gameComponentData.getParamsAsString() + ": ");
        // always put independent components at top
        if (this.params.length == 0) {
            System.out.println("UP");
            return -1;
        }

        // if this already is an instance it can go on top
        if (this.instance != null) {
            System.out.println("UP");
            return -1;
        }

        // check if this component depends on itself
        this.checkForSelfDependencyCyclingIssues();

        // check if there's a cycling dependency based on param names and other component's name
        for (String param : this.paramsName) {
            if (param.equals(gameComponentData.getName())) {
                for (String paramB : gameComponentData.getParamsName()) {
                    if (paramB.equals(this.getName())) {
                        throw new InstantiationError("both methods require each other (cycling dependency): " + this.getName() + ", " + gameComponentData.getName());
                    }
                }
            }
        }

        // if the other component depends on this, move up
        if (gameComponentData.dependsOnComponent(this.getName())) {
            System.out.println("UP");
            return -1;
        }

        // if this depends on other, move down
        if(this.dependsOnComponent(gameComponentData.getName())) {
            System.out.println("DOWN");
            return 1;
        }

        // otherwise our order / priority doesn't need to change
        System.out.println("-");
        return 0;
    }
}
