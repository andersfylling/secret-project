package team.adderall.game;

import android.app.Activity;
import android.graphics.Point;

import com.google.android.gms.games.Game;

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
import team.adderall.game.framework.multiplayer.GamePacket;
import team.adderall.game.framework.multiplayer.GamePacketResponse;
import team.adderall.game.framework.multiplayer.Packet;
import team.adderall.game.userinput.Jumping;

/**
 * Sends updates about main player and keep opponents up to date.
 */
@GameComponent
@GameLogic(wave=100)
public class Multiplayer
    implements GameLogicInterface
{
    public final static long NOT_REAL_GAME_ID = 0;
    private final static long TIMEOUT = 5;

    private final GameDetails gameDetails;
    private final Map<Integer, Player> gamers;
    private final Player gamer;
    private final Players players;

    private long sequence;
    private long gameSequence;

    private final boolean onlineGame;
    private final long gameID;

    private long lastUpdate;
    private boolean singleplayer;

    private int failedAuthTries;

    private Client client;
    private Activity activity;

    private final UserInputHolder userInputHolder;

    @GameDepWire
    public Multiplayer(@Inject("players") Players players,
                       @Inject("userInputHolder") UserInputHolder holder,
                       @Inject("activity") Activity activity,
                       @Inject("GameDetails") GameDetails gameDetails)
    {
        this.gameDetails = gameDetails;
        this.lastUpdate = System.currentTimeMillis() - TIMEOUT;

        this.onlineGame = gameDetails.isMultiplayer() || gameDetails.getGameID() == NOT_REAL_GAME_ID;
        this.gameID = gameDetails.getGameID();

        this.activity = activity;

        this.players = players;
        this.gamer = this.players.getActive();
        this.gamers = new HashMap<>();
        userInputHolder = holder;

        sequence = 1;
        gameSequence = 1;

        failedAuthTries = 0;

        singleplayer = !gameDetails.isMultiplayer();

        // don't populate or register listeners if this is not a real online game
        if (this.deactivated()) {
            return;
        }

        for (Player player : players.getAlivePlayers()) {
            this.gamers.put((int) player.getUserID(), player);
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
        GamePacket packet = GamePacket.AuthenticateBuilder(sequence++, (int) gamer.getUserID(), (int) gameID)
                .token(gamer.getGameToken())
                .build();
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

    public void eventHandler(final GamePacketResponse evt) {

        // check if there was an issue with the params
        boolean err = false;
        if (evt.type() == GamePacket.TYPE_UNKNOWN_GAME_ID) {
            System.out.println("UNKNOWN GAME ID");
            err = true;
        }
        else if (evt.type() == GamePacket.TYPE_UNKNOWN_USER_ID) {
            System.out.println("UNKNOWN USER ID");
            err = true;
        }
        else if (evt.type() == GamePacket.TYPE_ATHENTICATION_FAILED) {
            System.out.println("AUTH FAILED");
            failedAuthTries++;
            if (failedAuthTries > 30) {
                client.close();
                System.out.println("UNABLE TO AUTHENTICATE");
                activity.onBackPressed();
            }
            err = true;
        }
        if (err) {
            authenticate();
            return;
        }


        // update sequence number
        if (evt.sequence() > gameSequence) {
            gameSequence = evt.sequence();
        } else {
            // ignore packets with "outdated info" (compared to what we already got)
            return;
        }

        // check if player exists
        if (!this.gamers.containsKey(evt.userID())) {
            return;
        }

        // check if the event is for this player
        if (this.gamer.getUserID() == evt.userID()) {
            return; // TODO: update oneself to completely sync units
        }

        Player player = this.gamers.get(evt.userID());
        if (player.getBallManager().getState() == BallManager.STATE_DEAD) {
            return;
        }

        // handle movements
        if (evt.type() == GamePacket.TYPE_MOVEMENT) {
            handleMovementEvent(evt);
        }

    }

    public void authenticate() {
        GamePacket packet = GamePacket.AuthenticateBuilder(sequence++, (int) gamer.getUserID(), (int) gameID)
                .token(gamer.getGameToken())
                .build();
        client.send(packet);
    }

    public void handleMovementEvent(final GamePacketResponse evt) {

        //System.out.println("player[" + Long.toString(evt.getUserID()) + "]{air:" + (evt.isJumping() ? "true" : "false") + ", xDiff:" + Double.toString(movementXDiff) + "}");

        userInputHolder.requestMPXAxisMovement(evt.userID(), evt.x());

        boolean jumping = evt.inAir();
        if (jumping) {
            userInputHolder.requestJump(evt.userID());
        }
    }

    /**
     * Send out info about main player
     */
    @Override
    public void run() {
        // TODO: improve
        if (singleplayer) {
            return;
        }
        if (gamer == null) {
            return;
        }

        if (lastUpdate + TIMEOUT > System.currentTimeMillis()) {
            return;
        }
        lastUpdate = System.currentTimeMillis();


        BallManager bm = gamer.getBallManager();
        boolean jumping = bm.getVelocity() < 0;
        double x = bm.getX();
        double y = bm.getY();

        GamePacket packet = GamePacket.MovementBuilder(sequence++, (int) gamer.getUserID(), (int) gameID)
                .x(x)
                .y(y)
                .inAir(jumping)
                .build();
        client.send(packet);
    }

    public void close() {
        client.close();
    }
}
