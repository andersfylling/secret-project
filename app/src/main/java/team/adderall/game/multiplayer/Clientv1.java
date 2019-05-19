package team.adderall.game.multiplayer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class Clientv1
    implements Client
{
    private DatagramSocket socket;
    private InetAddress address;
    private int port;
    private boolean configured;

    private final List<EventListener> listeners;

    private Thread listener;
    private boolean listen;


    public Clientv1()
            throws SocketException
    {
        socket = new DatagramSocket();
        this.listeners = new ArrayList<>();
        this.configured = false;
    }

    @Override
    public void configure(String address, int port)
            throws UnknownHostException
    {
        this.address = InetAddress.getByName(address);
        this.port = port;
        this.configured = true;
    }

    @Override
    public void connect() {
        if (!this.configured) {
            throw new RuntimeException("socket client not configured to connect to a server");
        }
        this.listen = true;
        this.listener = new Thread(this::listenForEvents);
        this.listener.start();
    }

    @Override
    public void close() {
        if (listener.isInterrupted()) {
            return;
        }
        listen = false;
        listener.interrupt();
        socket.close();
        try {
            listener.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void send(GamePacket packet) {
        if (socket.isClosed()) {
            return;
        }

        byte[] buf = packet.getBuffer();

        DatagramPacket p = new DatagramPacket(buf, buf.length, address, 3173);
        try {
            socket.send(p);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public InetAddress getAddress() {
        return address;
    }

    @Override
    public void receive(EventListener listener) {
        this.listeners.add(listener);
    }

    private void listenForEvents() {
        while (listen) {
            byte[] buf = new byte[GamePacket.BUFFER_SIZE];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            try {
                socket.receive(packet);
            } catch (SocketException e) {
                break;
            } catch (IOException e) {
                e.printStackTrace();
            }

            byte[] data = packet.getData();
            GamePacketResponse response = new GamePacketResponse(data);

            // TODO: thread
            for (EventListener listener : this.listeners) {
                listener.trigger(response);
            }
        }

        System.out.println("No longer listening for socket packets");
    }
}
