package team.adderall.game.framework;

import android.os.Process;

import team.adderall.game.framework.component.GameComponent;
import team.adderall.game.framework.component.GameDepWire;
import team.adderall.game.framework.component.Inject;

@GameComponent("GameLoop")
public class GameLoop
        implements Runnable
{
    public final static int MAX_FRAMESKIP = 200;
    public final static int FPS = 60;
    public final static int TIMEOUT_MS = 1000 / FPS;

    //private CountDownLatch latch;
    //private final ExecutorService logicerThreadPool;
    private GameLogicInterface[][] logics; // populated by GameLogicManager at runtime
    private final GameLogicManager gameLogicManager;
    private final GraphicsManager painter;

    private UpdateRateCounter lps;
    private UpdateRateCounter fps;

    private boolean running;
    private long nextRun;

    @GameDepWire
    public GameLoop(
            @Inject("gameLogicManager") final GameLogicManager gameLogicManager,
            @Inject("GraphicsManager") final GraphicsManager gamePaintWrapper
    ) {
        this.gameLogicManager = gameLogicManager;
        this.painter = gamePaintWrapper;

//        int requiredThreads = 0;
//        for (GameLogicInterface[] wave : logics) {
//            if (wave.length > requiredThreads) {
//                requiredThreads = wave.length;
//            }
//        }
        //this.logicerThreadPool = Executors.newFixedThreadPool(requiredThreads);

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
            //this.latch = new CountDownLatch(wave.length);
            for (final GameLogicInterface job : wave) {
            //    this.logicerThreadPool.execute(() -> {
            //        job.run();
            //        latch.countDown();
            //    });
                if (job == null) {
                    break; // @see GameLogicManager#getGameLogicInterfacesAsArray array init.
                }
                job.run();
            }

            // wait for jobs to finish
            //latch.await(remainingMilliSeconds, TimeUnit.MILLISECONDS);
            //this.logicerThreadPool.awaitTermination(remainingMilliSeconds, TimeUnit.MILLISECONDS);
            remainingMilliSeconds -= System.currentTimeMillis() - start; // subtract used time

            // finished wave, go to next wave
        }
    }

    @Override
    public void run() {
        android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

        // populate logics
        this.logics = this.gameLogicManager.getGameLogicInterfacesAsArray();

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

    public void close() {
        this.running = false;
    }

    public void setLps(UpdateRateCounter lps) {
        this.lps = lps;
    }

    public void setFps(UpdateRateCounter fps) {
        this.fps = fps;
    }
}
