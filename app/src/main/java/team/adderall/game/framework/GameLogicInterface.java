package team.adderall.game.framework;

/**
 * Note! a logic instance can never be reached in a clear fashion.
 *       A better way would be to let the logic instances request/set data to other instances.
 *
 * Let the logic instance extract data from a source, process it, and update an object.
 * Once complete painting will execute.
 */
public interface GameLogicInterface
    extends Runnable
{}
