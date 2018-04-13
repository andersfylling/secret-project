package team.adderall.game.framework.context;

/**
 * Use this when creating new objects in the game configuration class.
 * This will throw an exception if an error occurred to help us avoid null pointers exceptions.
 */
public interface GameContextGetterAssured
    extends GameContextGetter
{
    Object getAssuredInstance(final String name);
}
