package team.adderall.game;

import android.graphics.Point;

import java.math.BigInteger;
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
@GameComponent
@GameLogic(wave=100)
public class Multiplayer
    implements GameLogicInterface
{
    public final static long NOT_REAL_GAME_ID = 0;
    private final static long TIMEOUT = 5;

    private final GameDetails gameDetails;
    private final Map<Long, Player> gamers;
    private final Player gamer;
    private final Players players;

    private final boolean onlineGame;
    private final long gameID;

    private long lastUpdate;

    private Client client;

    @GameDepWire
    public Multiplayer(@Inject("players") Players players,
                       @Inject("GameDetails") GameDetails gameDetails)
    {
        this.gameDetails = gameDetails;
        this.lastUpdate = System.currentTimeMillis() - TIMEOUT;

        this.onlineGame = gameDetails.isMultiplayer() || gameDetails.getGameID() == NOT_REAL_GAME_ID;
        this.gameID = gameDetails.getGameID();

        this.players = players;
        this.gamer = this.players.getActive();
        this.gamers = new HashMap<>();

        // don't populate or register listeners if this is not a real online game
        if (this.deactivated()) {
            return;
        }

        for (Player player : players.getAlivePlayers()) {
            this.gamers.put(player.getUserID(), player);
        }

        // listen for player updates (death, lost, win, etc. not moves from the udp server)
        this.players.registerListener(this::playerUpdate);

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
            this.client.configure(gameDetails.getGameServer(), gameDetails.getGameServerPort());
        } catch (UnknownHostException e) {
            System.err.println(e.getMessage());
        }
        this.client.connect();

        // register player
        Packet packet = new Packet(Packet.TYPE_REGISTER, players.getActive().getGameToken());
        client.send(packet);
    }

    public void playerUpdate(Player player, int action) {
        switch (action) {
            case PlayerChange.LOST:
            case PlayerChange.DIED:
                this.removeGamer(player.getUserID());
                break;
        }
    }

    private void removeGamer(long key) {
        if (this.gamers.containsKey(key)) {
            this.gamers.remove(key);
        }
    }

    public boolean deactivated() {
        return !this.onlineGame;
    }

    public void eventHandler(final Packet evt) {
        // check if player exists
        if (!this.gamers.containsKey(evt.getUserID())) {
            return;
        }

        // check if the event is for this player
        if (this.gamer.getUserID() == evt.getUserID()) {
            return; // TODO: update oneself to completely sync units
        }

        Player player = this.gamers.get(evt.getUserID());
        player.getBallManager().setPos(new Point(evt.getX(), evt.getY()));
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

        if (this.gamer == null) {
            return;
        }

        BallManager bm = this.gamer.getBallManager();
        boolean jumping = bm.getVelocity() < 0;
        int x = bm.getPos().x;
        int y = bm.getPos().y;

        // TODO: refactor
        Packet event = new Packet(Packet.TYPE_PLAYER_MOVED, x, y, jumping, this.gamer.getUserID(), this.gameID);

        this.client.send(event);
    }

    public void close() {
        this.client.close();
    }
}
