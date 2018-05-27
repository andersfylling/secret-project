package team.adderall.game.player;

public interface PlayerChange {
    int DIED = 1;
    int RESPAWN = 2;
    int WON = 3;
    int LOST = 4;
    int LOST_CONNECTION = 10;
    int RECONNECTED = 11;
    int CONNECTED = 12; // joined game

    void trigger(Player player, int action);
}
