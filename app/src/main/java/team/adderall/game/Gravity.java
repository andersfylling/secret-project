package team.adderall.game;

import android.graphics.Point;

import java.util.concurrent.TimeUnit;

import team.adderall.game.ball.BallManager;
import team.adderall.game.framework.GameLogicInterface;
import team.adderall.game.framework.GameLoop;
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
        long now = System.nanoTime();

        double diff = (now - this.lastRun) / 1000000000.0;
        this.lastRun = now;
        double acceleration = GRAVITY * diff;

        for(Player player : players.getAlivePlayers()){
            BallManager b = player.getBallManager(); // holds position info
            double velocity = b.getVelocity();

            Point pos = b.getPos();
            pos.set(pos.x, pos.y + (int)(velocity * (diff * GameLoop.FPS)));
            b.setPos(pos);

            velocity += acceleration;
            if(velocity > TERMINALVEL) {
                velocity = TERMINALVEL;
            }
            b.setVelocity(velocity);
        }
    }
}
