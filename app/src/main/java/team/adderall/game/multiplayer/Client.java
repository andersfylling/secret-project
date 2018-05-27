package team.adderall.game.multiplayer;

import java.net.UnknownHostException;

public interface Client {
    void configure(final String address, final int port) throws UnknownHostException;
    void connect();
    void close();

    void send(final GamePacket packet);
    void receive(final EventListener listener);
}
