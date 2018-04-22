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
    private double GRAVITY = 9.8;
    private double TERMINALVEL = 30;
    private long lastRun;

    @GameDepWire
    public Gravity(@Inject("players") Players p) {
        this.players = p;
        this.lastRun = System.nanoTime();
    }

    @Override
    public void run() {
        for(Player player : players.getAlivePlayers()){
            BallManager b = player.getBallManager();
            long now = System.nanoTime(); // inaccurate af

            double diff = (now - this.lastRun) / 1000000000.0;
            this.lastRun = now;

            double velocity = b.getVelocity() + GRAVITY * diff;
            if(velocity > (int)TERMINALVEL){
                velocity = (int)TERMINALVEL;
            }
            b.setVelocity(velocity);

            Point pos = b.getPos();
            pos.set(pos.x, pos.y + (int)(velocity));
            b.setPos(pos);
        }
    }
}
