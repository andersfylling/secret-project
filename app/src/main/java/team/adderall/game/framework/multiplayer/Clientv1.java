package team.adderall.game.framework.multiplayer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
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
        if (Packet.VERSION != 1) {
            throw new Error("WRONG VERSION ON PACKET CLASS");
        }

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
    public void send(Packet packet) {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        long data = packet.getAsLong();
        buffer.putLong(data);

        byte[] buf = buffer.array();

        DatagramPacket p = new DatagramPacket(buf, buf.length, address, 3173);
        try {
            socket.send(p);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void receive(EventListener listener) {
        this.listeners.add(listener);
    }

    private void listenForEvents() {
        while (listen) {
            byte[] buf = new byte[8];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            try {
                socket.receive(packet);
            } catch (SocketException e) {
                break;
            } catch (IOException e) {
                e.printStackTrace();
            }

            byte[] data = packet.getData();

            ByteBuffer buffer = ByteBuffer.allocate(8);
            buffer.put(data);
            buffer.flip();//need flip
            long convertedData = buffer.getLong();
            Packet event = new Packet(convertedData);

            for (EventListener listener : this.listeners) {
                listener.trigger(event);
            }
        }

        System.out.println("No longer listening for socket packets");
    }
}
