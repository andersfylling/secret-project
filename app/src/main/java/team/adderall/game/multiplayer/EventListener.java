package team.adderall.game.multiplayer;

public interface EventListener {
    void trigger(final GamePacketResponse packet);
}
