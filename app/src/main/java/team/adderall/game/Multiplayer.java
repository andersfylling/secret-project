package team.adderall.game;

import android.graphics.Point;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import team.adderall.game.ball.BallManager;
import team.adderall.game.framework.GameLogicInterface;
import team.adderall.game.framework.component.GameComponent;
import team.adderall.game.framework.component.GameDepWire;
import team.adderall.game.framework.component.GameLogic;
import team.adderall.game.framework.component.Inject;
import team.adderall.game.framework.multiplayer.Client;
import team.adderall.game.framework.multiplayer.Clientv1;
import team.adderall.game.framework.multiplayer.Packet;

/**
 * Sends updates about main player and keep other players up to date.
 */
@GameComponent("multiplayer")
@GameLogic(wave=5)
public class Multiplayer
    implements GameLogicInterface
{
    private final static String SERVER_ADDRESS = "10.0.0.87"; //config file?
    private final static int SERVER_PORT = 3173;
    private final static long TIMEOUT = 20;

    private final Map<Long, BallManager> players;
    private final Players playersObj;
    private long gameID;
    private long id;


    private long lastUpdate;


    private Client client;

    @GameDepWire
    public Multiplayer(
            @Inject("players") Players players
    ) {
        this.lastUpdate = System.currentTimeMillis() - TIMEOUT;
        this.gameID = 23; // TODO: ask server
        this.playersObj = players;
        this.players = new HashMap<>();
        this.id = -1;

        // TODO: improve: let DI deal with exceptions
        try {
            this.client = new Clientv1();
        } catch (SocketException e) {
            System.err.println(e.getMessage());
        }

        if (this.client == null) {
            return;
        }
        this.client.receive(this::eventHandler);
        try {
            this.client.configure(SERVER_ADDRESS, SERVER_PORT);
        } catch (UnknownHostException e) {
            System.err.println(e.getMessage());
        }
        this.client.connect();

        // register our player

    }

    private void updatePlayers() {

    }

    public void eventHandler(final Packet evt) {
        // check if player exists
        if (!this.players.containsKey(evt.getUserID())) {
            // register a new player
            // TODO: development ONLY
            BallManager newPlayer = new BallManager(false);
            newPlayer.getBall().setColour("#1144aa");
            this.playersObj.registerNewPlayer(newPlayer);
            this.players.put(evt.getUserID(), newPlayer);
        }

        // check if the event is for this player
        if (this.players.get(evt.getUserID()).isActivePlayer()) {
            return;
        }

        System.err.println("NOOOOOOOOOOOOOOOOOOOO\n\n");
        BallManager player = this.players.get(evt.getUserID());
        player.setPos(new Point(evt.getX(), evt.getY()));
    }

    /**
     * Send out info about main player
     */
    @Override
    public void run() {
        if (this.lastUpdate + TIMEOUT > System.currentTimeMillis()) {
            return;
        }
        this.lastUpdate = System.currentTimeMillis();

        BallManager player = this.playersObj.getActive();
        boolean jumping = player.getVelocity() < 0;
        int x = player.getPos().x;
        int y = player.getPos().y;

        Packet event = new Packet(Packet.TYPE_PLAYER_MOVED, x, y, jumping, 0, this.gameID);

        this.client.send(event);
    }
}
