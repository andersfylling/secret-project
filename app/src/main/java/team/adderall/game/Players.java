package team.adderall.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import team.adderall.game.ball.BallManager;
import team.adderall.game.framework.component.GameComponent;
import team.adderall.game.framework.component.GameDepWire;
import team.adderall.game.framework.component.Inject;

@GameComponent
public class Players
    implements SensorEvtListener
{
    private final List<Player> players;
    private List<Player> alivePlayers;
    private List<Player> deadPlayers;
    private final GameDetails details;

    private Player active;
    private List<PlayerChange> listeners;

    @GameDepWire
    public Players(@Inject("GameDetails") GameDetails gameDetails)
    {
        this.details = gameDetails;
        this.players = new ArrayList<>();
        this.deadPlayers = new ArrayList<>();
        this.alivePlayers = new ArrayList<>();
        this.active = null;
        this.listeners = new ArrayList<>();

        // add players
        for (Map.Entry<Long, Player> entry : details.getPlayers().entrySet()) {
            Player player = entry.getValue();
            player.createBallManager(player.isActivePlayer());

            this.players.add(player);
            this.alivePlayers.add(player);
        }
    }

    public List<Player> getAlivePlayers() {
        return alivePlayers;
    }

    public List<Player> getDeadPlayers() {
        return deadPlayers;
    }

    // rewrite
    @Override
    public void onSensorEvt(SensorEvt evt) {
        if (this.active == null) {
            return;
        }

        this.getActive().getBallManager().onSensorEvt(evt);
    }

    public void invalidateActive() {
        this.active = null;
    }

    public Player getActive() {
        if (this.active == null) {
            for (Player player : players) {
                if (player.isActivePlayer()) {
                    this.active = player;
                    break;
                }
            }
        }
        return this.active;
    }

    public int size() {
        return this.players.size();
    }

    public void setToDead(Player player) {
        int index = this.alivePlayers.indexOf(player);
        if (index > -1) {
            this.alivePlayers.remove(index);
            this.deadPlayers.add(player);

            // trigger listeners
            this.triggerListenersThreaded(player, PlayerChange.DIED);
        }
    }



    public void registerListener(final PlayerChange listener) {
        this.listeners.add(listener);
    }

    public void triggerListeners(final Player player, final int action) {
        for (PlayerChange listener : this.listeners) {
            listener.trigger(player, action);
        }
    }
    public void triggerListenersThreaded(final Player player, final int action) {
        (new Thread(() -> {
            this.triggerListeners(player, action);
        })).start();
    }
}
