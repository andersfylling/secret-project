package team.adderall.game;

import team.adderall.game.framework.GameContext;

public class Game {

    private final LogicManager logic;
    private final PaintManager painter;

    public Game(final LogicManager logic, final PaintManager painter) {
        this.logic = logic;
        this.painter = painter;
    }
}
