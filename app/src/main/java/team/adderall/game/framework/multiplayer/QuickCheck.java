//package team.adderall.game.framework.multiplayer;
//
//import java.net.DatagramPacket;
//import java.net.SocketException;
//import java.net.UnknownHostException;
//
//public class QuickCheck {
//    public static void main(String[] args) throws SocketException, UnknownHostException, InterruptedException {
//        Clientv1 client = new Clientv1();
//
//        client.configure("10.0.0.87", 3173);
//        client.connect();
//
//        byte[] buf = GamePacket.MovementBuilder(34L, 1, 3)
//                .x(235234)
//                .y(3453)
//                .inAir(false)
//                .build();
//
//        DatagramPacket p = new DatagramPacket(buf, buf.length, client.getAddress(), 3173);
//        client.send(p);
//        client.receive(evt -> {
//            System.out.println(evt.toString());
//        });
//
//        //TimeUnit.SECONDS.sleep(1);
//        client.close();
//    }
//}
