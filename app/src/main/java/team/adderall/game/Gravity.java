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
    private long lastRun;


    @GameDepWire
    public Gravity(@Inject("players") Players p) {
        this.players = p;
        this.lastRun = System.nanoTime();
    }


    @Override
    public void run() {
        for(BallManager b : players.toList()){
            long now = System.nanoTime();

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


            //b.setPos(gravity(b.getPos()));
        }
    }

/*    *//**
     * Gravity implemetation.
     * Fall to the ground at a constant rate.
     *//*
    public Point gravity(Point pos) {
        int y = pos.y;
        long now = System.nanoTime();

        double diff = now - this.lastRun;
        double acceleration = 9.8;
        double velocity += acceleration * diff;

        this.verticalSpeed += GRAVITY * 0.5 * ();

        if(this.verticalSpeed > TERMINALVEL){
            this.verticalSpeed = TERMINALVEL;
        }
        y += this.verticalSpeed;
        pos.set(pos.x,y);
        return pos;
    }*/

}
