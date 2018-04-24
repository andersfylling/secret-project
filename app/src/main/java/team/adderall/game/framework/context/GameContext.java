package team.adderall.game.framework.context;

import java.util.HashMap;
import java.util.Map;

public class GameContext
    implements
        GameContextGetterAssured,
        GameContextGetter,
        GameContextSetter
{
    public final static String NAME = "MAIN_GAME_CONTEXT";
    public final static String LOGIC = "game logic";
    public final static String PAINT = "game painters";
    public final static String SENSOR_EVENT = "sensor event manager";

    private final Map<String, Object> instances;

    public GameContext() {
        this.instances = new HashMap<>();
    }

    @Override
    public void setInstance(final String name, final Object instance) {
        if (name == null || name.isEmpty()) {
            String err = "unable to store class instance without a name for object: " + instance.toString();
            throw new RuntimeException(err);
        }
        if (instance == null) {
            String err = "cannot store null instance";
            throw new RuntimeException(err);
        }
        this.instances.put(name, instance);
    }

    @Override
    public Object getInstance(final String name) {
        if (this.instances.containsKey(name)) {
            return this.instances.get(name);
        }

        return null;
    }

    @Override
    public Object getAssuredInstance(final String name) {
        Object instance = this.getInstance(name);

        if (instance == null) {
            final String err = String.format("no instance with the name %s exists", name);
            throw new InstantiationError(err);
        }

        return instance;
    }

    public Map<String, Object> getInstances() {
        return this.instances;
    }

    public int size() {
        return this.instances.size();
    }
}
