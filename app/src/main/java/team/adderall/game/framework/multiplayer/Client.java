package team.adderall.game.framework.multiplayer;

import java.net.UnknownHostException;

public interface Client {
    void configure(final String address, final int port) throws UnknownHostException;
    void connect();
    void close();

    void send(final Packet packet);
    void receive(final EventListener listener);
}
