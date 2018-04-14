package team.adderall.game;

import java.util.ArrayList;
import java.util.List;

import team.adderall.game.ball.BallManager;
import team.adderall.game.framework.component.GameComponent;
import team.adderall.game.framework.component.GameDepWire;

@GameComponent("players")
public class Players {
    private final List<BallManager> players;

    @GameDepWire
    public Players() {
        this.players = new ArrayList<>();
        players.add(new BallManager(true));
    }

    public List<BallManager> toList() {
        return players;
    }
}
