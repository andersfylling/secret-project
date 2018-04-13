package team.adderall.game.framework;

public interface FullGameRegister
    extends
        GameRegister,
        BasicGameRegister
{
    Manager setSensorEventManager();
    Manager setSensorEventManager(final GameContextGetterAssured ctx);
}
