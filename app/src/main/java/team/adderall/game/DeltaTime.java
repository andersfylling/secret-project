package team.adderall.game;

import java.util.concurrent.TimeUnit;

import team.adderall.game.framework.GameLogicInterface;
import team.adderall.game.framework.component.GameComponent;
import team.adderall.game.framework.component.GameDepWire;
import team.adderall.game.framework.component.GameLogic;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

/**
 * Delta time in _nano seconds_
 */
@GameComponent
@GameLogic(wave = 1)
public class DeltaTime
    implements
    GameLogicInterface
{
    private long last;
    private long dT;
    private double speed;

    @GameDepWire
    public DeltaTime() {
        last = System.nanoTime() - 1000000;
        dT = 0;
        speed = 1;
    }


    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        long now = System.nanoTime();

        dT = now - last;
        dT *= speed;

        last = now;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }
    public double getSpeed() {
        return speed;
    }

    public long getDiff() {
        return dT;
    }

    public double getDiff(TimeUnit timeUnit) {
        double multiplier = 1.0;
        switch (timeUnit) {
            case NANOSECONDS:
                multiplier = 1.0;
                break;
            case MICROSECONDS:
                multiplier = 1000.0;
                break;
            case MILLISECONDS:
                multiplier = 1000000.0;
                break;
            case SECONDS:
                multiplier = 1000000000.0;
                break;
            default:
        }

        return dT / multiplier;
    }

    public long getRoundedDiff(TimeUnit timeUnit) {
        return timeUnit.convert(dT, TimeUnit.NANOSECONDS);
    }

    public long getLastRun() {
        return last;
    }


}
