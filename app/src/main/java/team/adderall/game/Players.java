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
    private BallManager active;

    @GameDepWire
    public Players() {
        this.players = new ArrayList<>();
        this.active = new BallManager(true);

        players.add(this.active); // use a registration service to handle multiplayer logic (?)
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
}
