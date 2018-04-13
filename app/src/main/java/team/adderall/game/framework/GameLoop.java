package team.adderall.game.framework;

import android.os.Process;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class GameLoop
        implements Runnable
{
    public final static int MAX_FRAMESKIP = 100;
    public final static int FPS = 60;
    public final static int TIMEOUT_MS = 1000 / FPS;


    private final ExecutorService logicerThreadPool;
    private final Logicer[][] logics;
    private final Painter[][] painters;

    private boolean running;
    private long nextRun;

    public GameLoop(final Logicer[][] logics, final Painter[][] painters) {
        this.logics = logics;
        this.painters = painters;

        int requiredThreads = 0;
        for (Logicer[] wave : logics) {
            if (wave.length > requiredThreads) {
                requiredThreads = wave.length;
            }
        }
        this.logicerThreadPool = Executors.newFixedThreadPool(requiredThreads);

        this.running = true;
        this.nextRun = System.currentTimeMillis();
    }

    /**
     *
     * @param remainingMilliSeconds
     * @throws InterruptedException
     */
    private void executeLogicInWaves(long remainingMilliSeconds)
            throws InterruptedException
    {
        for (Logicer[] wave : this.logics) {
            final long start = System.currentTimeMillis();
            // run jobs in parallel
            for (Logicer job : wave) {
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
                    this.executeLogicInWaves(nextRun - System.currentTimeMillis());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    // TODO: improve? somehow? help
                }
                loops++;
            }

            nextRun += TIMEOUT_MS;
            //this.paintListener.executeGamePaint();
            System.out.println("painting");
        }
    }

    public void stopGameLoop() {
        this.running = false;
    }
}
