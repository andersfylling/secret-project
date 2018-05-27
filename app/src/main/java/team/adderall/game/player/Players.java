package team.adderall.game.player;

import java.util.ArrayList;
import java.util.List;

import addy.annotations.*;
import team.adderall.game.GameDetails;

@Service
public class Players
{
    private final List<Player> players;
    private List<Player> alivePlayers;
    private List<Player> deadPlayers;
    private final GameDetails details;

    private Player active;
    private List<PlayerChange> listeners;

    @DepWire
    public Players(@Inject("GameDetails") GameDetails gameDetails)
    {
        this.details = gameDetails;
        this.players = new ArrayList<>();
        this.deadPlayers = new ArrayList<>();
        this.alivePlayers = new ArrayList<>();
        this.active = null;
        this.listeners = new ArrayList<>();

        // add players
        for (Player player : details.getPlayers()) {
            player.createBallManager();

            this.players.add(player);
            this.alivePlayers.add(player);

            if (player.isActivePlayer()) {
                this.active = player;
            }
        }
    }

    public List<Player> getAlivePlayers() {
        return alivePlayers;
    }

    public List<Player> getDeadPlayers() {
        return deadPlayers;
    }

    public List<Player> getPlayers(){ return players;}

    public void invalidateActive() {
        this.active = null;
    }

    public synchronized Player getActive() {
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

    public synchronized void setToDead(Player player) {
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
        (new Thread(() -> this.triggerListeners(player, action))).start();
    }
}
