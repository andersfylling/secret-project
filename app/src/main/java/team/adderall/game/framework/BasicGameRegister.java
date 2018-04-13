package team.adderall.game.framework;

public interface BasicGameRegister
        extends GameRegister
{
    Logicer[][] setLogicManager();
    Logicer[][] setLogicManager(final GameContextGetterAssured ctx);

    Manager[][] setPaintManager();
    Manager[][] setPaintManager(final GameContextGetterAssured ctx);
}
