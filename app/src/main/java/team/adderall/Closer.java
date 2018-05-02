package team.adderall;

/**
 * Used by the dependency injector to close/destroy
 * any object when the game stops.
 */
public interface Closer {
    /**
     * Run by the Injector to close/destroy the object.
     */
    void close();
}
