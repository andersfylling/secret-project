package team.adderall.game.framework;

import android.os.Process;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import team.adderall.game.framework.component.GameComponent;
import team.adderall.game.framework.component.GameDepWire;
import team.adderall.game.framework.component.Inject;
import team.adderall.game.framework.context.GameContext;

@GameComponent
public class GameLoop
        implements Runnable
{
    public final static int MAX_FRAMESKIP = 100;
    public final static int FPS = 60;
    public final static int TIMEOUT_MS = 1000 / FPS;


    private final ExecutorService logicerThreadPool;
    private final GameLogicInterface[][] logics;
    private final GamePaintWrapper painter;

    private UpdateRateCounter lps;
    private UpdateRateCounter fps;

    private boolean running;
    private long nextRun;

    @GameDepWire
    public GameLoop(
            @Inject(GameContext.LOGIC) final GameLogicInterface[][] logics,
            @Inject("gamePaintWrapper") final GamePaintWrapper gamePaintWrapper
    ) {
        this.logics = logics;
        this.painter = gamePaintWrapper;

        int requiredThreads = 0;
        for (GameLogicInterface[] wave : logics) {
            if (wave.length > requiredThreads) {
                requiredThreads = wave.length;
            }
        }
        this.logicerThreadPool = Executors.newFixedThreadPool(requiredThreads);

        this.running = true;
        this.nextRun = System.currentTimeMillis();

        this.lps = null;
        this.fps = null;
    }

    /**
     *
     * @param remainingMilliSeconds
     * @throws InterruptedException
     */
    private void executeLogicInWaves(long remainingMilliSeconds)
            throws InterruptedException
    {
        for (GameLogicInterface[] wave : this.logics) {
            final long start = System.currentTimeMillis();
            // run jobs in parallel
            for (GameLogicInterface job : wave) {
                this.logicerThreadPool.execute(job);
            }

            // wait for jobs to finish
            this.logicerThreadPool.awaitTermination(remainingMilliSeconds, TimeUnit.MILLISECONDS);
            remainingMilliSeconds -= System.currentTimeMillis() - start; // subtract used time

            // finished wave, go to next wave
        }
    }

    @Override
    public void run() {
        android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

        while (this.running) {
            int loops = 0;
            while (System.currentTimeMillis() < nextRun && loops < MAX_FRAMESKIP) {
                try {
                    this.executeLogicInWaves(nextRun - System.currentTimeMillis() + 1);
                    if (this.lps != null) {
                        this.lps.update();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    // TODO: improve? somehow? help
                }
                loops++;
            }

            nextRun += TIMEOUT_MS;
            this.painter.redraw(); // threaded, might need to synchronize
            if (this.fps != null) {
                this.fps.update();
            }
        }
    }

    public void stopGameLoop() {
        this.running = false;
    }

    public void setLps(UpdateRateCounter lps) {
        this.lps = lps;
    }

    public void setFps(UpdateRateCounter fps) {
        this.fps = fps;
    }
}
