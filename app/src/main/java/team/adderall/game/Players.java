package team.adderall.game;

import java.util.ArrayList;
import java.util.List;

import team.adderall.game.ball.BallManager;
import team.adderall.game.framework.component.GameComponent;
import team.adderall.game.framework.component.GameDepWire;

@GameComponent("players")
public class Players
    implements SensorEvtListener
{
    // TODO: sort players based on email, android id, or whatever.
    private final List<BallManager> players;
    private List<BallManager> alivePlayers;
    private List<BallManager> deadPlayers;

    private BallManager active;

    @GameDepWire
    public Players() {
        this.players = new ArrayList<>();
        this.deadPlayers = new ArrayList<>();

        this.active = new BallManager(true);
        players.add(this.active); // use a registration service to handle multiplayer logic (?)

        /**
         * This.alivePlayers is a copy of the players array.
         * We need both as some things, e.g drawing fps/highscore.. should be done regardless of player state
         * Atleast for now.
         */
        this.alivePlayers = new ArrayList<>(players);
    }

    public List<BallManager> toList() {
        return players;
    }

    @Override
    public void onSensorEvt(SensorEvt evt) {
        for (BallManager manager : this.players) {
            manager.onSensorEvt(evt);
        }
    }

    public BallManager getActive() {
        return this.active;
    }

    public int size() {
        return this.players.size();
    }

    public void registerNewPlayer(BallManager newPlayer) {
        this.players.add(newPlayer);
    }

    public void setToDead(BallManager toDead) {
        alivePlayers.remove(toDead);
        deadPlayers.add(toDead);
    }

    public List<BallManager> getAlivePlayers() {
        return alivePlayers;
    }
}
