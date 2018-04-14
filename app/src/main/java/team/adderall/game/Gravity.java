package team.adderall.game;

import android.graphics.Point;

import team.adderall.game.ball.BallManager;
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
    private double verticalSpeed;
    private double GRAVITY = 9.8;
    private double TERMINALVEL = 30;
    static int FPS = 60;
    @GameDepWire
    public Gravity(@Inject("players") Players p) {
        this.players = p;
    }


    @Override
    public void run() {
        for(BallManager b : players.toList()){
            b.setPos(gravity(b.getPos()));
        }
    }

    /**
     * Gravity implemetation.
     * Fall to the ground at a constant rate.
     */
    public Point gravity(Point pos) {
        int y = pos.y;
        this.verticalSpeed += GRAVITY *1/FPS;//0.5* partOfSecond(System.currentTimeMillis());
        if(this.verticalSpeed > TERMINALVEL){
            this.verticalSpeed = TERMINALVEL;
        }
        y += this.verticalSpeed;
        pos.set(pos.x,y);
        return pos;
    }

}
