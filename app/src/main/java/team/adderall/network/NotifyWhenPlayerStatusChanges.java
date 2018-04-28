package team.adderall.network;

@FunctionalInterface
public interface NotifyWhenPlayerStatusChanges {
    void updatePlayerStatus(PlayerStatus playerStatus);
}
