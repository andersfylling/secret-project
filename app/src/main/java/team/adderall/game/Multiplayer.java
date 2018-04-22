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

    private final GameDetails gameDetails;
    private final Map<Long, BallManager> multiplayers;
    private final Players players;
    private long gameID;
    private long id;


    private long lastUpdate;


    private Client client;

    public Multiplayer(Players players, GameDetails gameDetails) {
        this.gameDetails = gameDetails;
        this.lastUpdate = System.currentTimeMillis() - TIMEOUT;

        this.players = players;

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

        // register player

    }

    private void updatePlayers() {

    }

    public void eventHandler(final Packet evt) {
        // check if player exists
        if (!this.players.containsKey(evt.getUserID())) {
            return;
        }

        // check if the event is for this player
        if (this.players.get(evt.getUserID()).isActivePlayer()) {
            return;
        }

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

        Player player = this.playersObj.getActive();
        if (player == null) {
            return;
        }

        BallManager bm = player.getBallManager();
        boolean jumping = bm.getVelocity() < 0;
        int x = bm.getPos().x;
        int y = bm.getPos().y;

        // TODO: refactor
        Packet event = new Packet(Packet.TYPE_PLAYER_MOVED, x, y, jumping, this.playersObj.getActive().getUserID(), this.playersObj.getActive().getGameID());

        this.client.send(event);
    }
}
