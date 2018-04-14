package team.adderall.game;

import team.adderall.game.framework.GameLogicInterface;
import team.adderall.game.framework.component.GameComponent;
import team.adderall.game.framework.component.GameDepWire;
import team.adderall.game.framework.component.GameLogic;
import team.adderall.game.framework.component.Inject;

@GameComponent
@GameLogic(wave = 1)
public class Gravity
    implements GameLogicInterface
{
    private final Players players;

    @GameDepWire
    public Gravity(@Inject("players") Players p) {
        this.players = p;
    }


    @Override
    public void run() {
        //System.out.println("running gravity logic");
    }
}
