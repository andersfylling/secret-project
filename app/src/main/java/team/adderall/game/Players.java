package team.adderall.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import team.adderall.game.ball.BallManager;
import team.adderall.game.framework.component.GameComponent;
import team.adderall.game.framework.component.GameDepWire;

@GameComponent("players")
public class Players
    implements SensorEvtListener
{
    private final Map<Long, Player> players;
    private Map<Long, Player> alivePlayers;
    private Map<Long, Player> deadPlayers;

    private Player active;

    @GameDepWire
    public Players() {
        this.players = new HashMap<>();
        this.deadPlayers = new HashMap<>();
        this.active = null;

        /**
         * This.alivePlayers is a copy of the players array.
         * We need both as some things, e.g drawing fps/highscore.. should be done regardless of player state
         * Atleast for now.
         */
        this.alivePlayers = new HashMap<>(players);
    }

    public List<Player> getAlivePlayersAsList() {
        List<Player> list = new ArrayList<>();
        for (Map.Entry<Long, Player> entry : alivePlayers.entrySet()) {
            list.add(entry.getValue());
        }

        return list;
    }
    public Map<Long, Player> getAlivePlayers() {
        return alivePlayers;
    }

    public List<Player> getDeadPlayersAsList() {
        List<Player> list = new ArrayList<>();
        for (Map.Entry<Long, Player> entry : deadPlayers.entrySet()) {
            list.add(entry.getValue());
        }

        return list;
    }
    public Map<Long, Player> getDeadPlayers() {
        return deadPlayers;
    }

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
            for (Map.Entry<Long, Player> entry : players.entrySet()) {
                if (entry.getValue().isActivePlayer()) {
                    this.active = entry.getValue();
                    break;
                }
            }
        }
        return this.active;
    }

    public int size() {
        return this.players.size();
    }

    /**
     * This method does not check with the game server, and can therefore NOT
     * be used in multiplayer.
     *
     * @param player
     * @return generated user_id
     */
    public long registerNewPlayer(Player player) {
        long userID = 243235; // random number
        while (players.containsKey(userID)) {
            userID++;
        }
        this.players.put(userID, player);
        this.alivePlayers.put(userID, player);

        return userID;
    }

    public void setToDead(long userID) {
        if (!alivePlayers.containsKey(userID)) {
            return;
        }

        Player deadGuy = alivePlayers.remove(userID);
        deadPlayers.put(userID, deadGuy);
    }

    public void registerPlayersWithUserID(Map<Long, Player> players) {
        this.players.putAll(players);
        this.alivePlayers.putAll(players);
    }
}
