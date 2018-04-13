package team.adderall.game.framework;

public interface GameRegister {
    void registerInstances(final GameContextGetterAssured ctx);
    void registerInstances(final GameContextGetterAssured ctx, final GameContextSetter setter);


}
