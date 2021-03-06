package team.adderall.game.framework.component;

import android.support.annotation.NonNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
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
    private final List<String> dependencies;

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

        this.dependencies = new ArrayList<>();
        this.dependencies.addAll(Arrays.asList(this.paramsName));
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
        this.dependencies = new ArrayList<>();
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
                } catch (IllegalAccessException | InvocationTargetException e) {
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
        for (String dependency : this.dependencies) {
            if (dependency.equals(component)) {
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
                        throw new InstantiationError(this.getName() + " expected it's dependency be instantiated: " + component.getName());
                    }

                    counter++;
                    break;
                }
            }
        }

        // check if any dependencies are missing
        if (dependencies.length > 0 && dependencies[dependencies.length - 1] == null) {
            String err = "missing dependencies for game component: " + this.getName();

            StringBuilder have = new StringBuilder();
            for (Object dep : dependencies) {
                if (dep == null) {
                    continue;
                }
                have.append(dep.toString()).append(",");
            }


            err += ": params" + this.getParamsAsString() + ": " + have;

            throw new InstantiationError(err);
        }

        // assumption: everything ok
        try {
            if (this.paramsName.length == 0) {
                this.instance = this.method.invoke(this.classInstance);
            } else {
                this.instance = this.method.invoke(this.classInstance, dependencies);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }


    public String getParamsAsString() {
        StringBuilder params = new StringBuilder("{");
        for (String param : this.paramsName) {
            params.append(param).append(",");
        }
        params.append("}");

        return params.toString();
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
                        triggerDependencyCyclingError(this.getName(), gameComponentData.getName());
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

    private void triggerDependencyCyclingError(String a, String b) {
        throw new InstantiationError("both methods require each other (cycling dependency): " + a + ", " + b);
    }

    public void updateDependencies(final List<GameComponentData> components) {
        this.updateDependencies(components, this.getName());
    }

    public void updateDependencies(final List<GameComponentData> components, final String root) {
        for (GameComponentData component : components) {
            // don't evaluate itself
            if (this.getName().equals(component.getName())) {
                continue;
            }

            // ignore if not a dependency
            if (!this.dependsOnComponent(component.getName())) {
                continue;
            }

            // if this is a dependency and it requires the root component,
            // its a cycling dependency issue
            if (!root.equals(this.getName()) && component.getName().equals(root)) {
                triggerDependencyCyclingError(root, this.getName());
            }

            // lay out the dependencies of the dependency
            component.updateDependencies(components, root);

            List<String> requiredDependencies = component.getDependencies();
            List<String> missingDependencies = new ArrayList<>();
            for (String want : requiredDependencies) {
                boolean alreadyGotIt = false;
                for (String got : this.dependencies) {
                    if (want.equals(got)) {
                        alreadyGotIt = true;
                        break;
                    }
                }

                if (!alreadyGotIt) {
                    missingDependencies.add(want);
                }
            }

            this.dependencies.addAll(missingDependencies);
        }
    }

    public List<String> getDependencies() {
        return this.dependencies;
    }

    public String getDependenciesAsJSON() {
        StringBuilder json = new StringBuilder("{");
        for (String d : this.dependencies) {
            json.append(d).append(",");
        }
        if (json.length() > 1) {
            json.deleteCharAt(json.length() - 1);
        }
        json.append("}");

        return json.toString();
    }



}
