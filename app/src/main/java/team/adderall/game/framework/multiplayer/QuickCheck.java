//package team.adderall.game.framework.multiplayer;
//
//import java.net.SocketException;
//import java.net.UnknownHostException;
//import java.util.concurrent.TimeUnit;
//
//public class QuickCheck {
//    public static void main(String[] args) throws SocketException, UnknownHostException, InterruptedException {
//        Client client = new Clientv1();
//
//        client.configure("10.0.0.87", 3173);
//        client.connect();
//
//        Packet packet = new Packet(Packet.TYPE_PLAYER_MOVED, 54, 123, true, 2, 45);
//        client.send(packet);
//        client.receive(evt -> {
//            System.out.println(evt.toString());
//        });
//
//        TimeUnit.SECONDS.sleep(1);
//        client.close();
//    }
//}
