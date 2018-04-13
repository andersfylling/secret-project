package team.adderall.game;

import java.util.ArrayList;
import java.util.List;

import team.adderall.game.framework.component.GameComponent;
import team.adderall.game.framework.component.GameDepWire;

@GameComponent("players")
public class Players {
    private final List<Player> players;

    @GameDepWire
    public Players() {
        this.players = new ArrayList<>();
    }
}
