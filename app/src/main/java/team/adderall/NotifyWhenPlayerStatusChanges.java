package team.adderall;

@FunctionalInterface
public interface NotifyWhenPlayerStatusChanges {
    void updatePlayerStatus(PlayerStatus playerStatus);
}
