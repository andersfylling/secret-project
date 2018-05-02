package team.adderall.game.framework.component;


@FunctionalInterface
public interface GameComponentInvoker {
    /**
     *
     * @param function
     * @param dependencies array of instantiated dependencies
     * @param classInstance if "function" is of type Method. otherwise set to null
     * @return
     */
    Object initiate(final Object function, final Object[] dependencies, final Object classInstance)
            throws
            Exception;
}
