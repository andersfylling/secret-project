package team.adderall.game.framework.multiplayer;

public interface EventListener {
    void trigger(final GamePacketResponse packet);
}
