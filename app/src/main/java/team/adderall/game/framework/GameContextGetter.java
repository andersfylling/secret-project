package team.adderall.game.framework;

/**
 * Use this interface during runtime and not the GameContextGetterAssured as this won't crash
 * the program when the instance was not found.
 */
public interface GameContextGetter {
    Object getInstance(final String name);
}
