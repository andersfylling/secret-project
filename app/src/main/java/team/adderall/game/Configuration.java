package team.adderall.game;

import android.app.Activity;
import android.graphics.Point;
import android.view.Display;

import team.adderall.game.ball.DrawBall;
import team.adderall.game.easyLogicChecks.PlayerDeathListHandler;
import team.adderall.game.framework.GameLoop;
import team.adderall.game.framework.GamePaintWrapper;
import team.adderall.game.framework.GamePainter;
import team.adderall.game.framework.UpdateRateCounter;
import team.adderall.game.framework.component.GameComponent;
import team.adderall.game.framework.configuration.GameConfiguration;
import team.adderall.game.framework.context.GameContext;
import team.adderall.game.framework.GameLogicInterface;
import team.adderall.game.framework.component.Inject;
import team.adderall.game.level.LevelManager;

/**
 * Reference this class in the GameActivity when initializing the game.
 */
@GameConfiguration
public class Configuration
{
//    @GameComponentRegister // TODO: implement logic. not currently working.
//    public void registerInstances(@Inject(GameContext.NAME) GameContextGetterAssured ctx, @Inject("GameContextSetter") GameContextSetter setter) {
//        LogicManager logic = (LogicManager) ctx.getAssuredInstance(GameContext.LOGIC);
//        PaintManager painter = (PaintManager) ctx.getAssuredInstance(GameContext.PAINT);
//
//        // register a new game instance
//        setter.setInstance("Game", new Game(logic, painter));
//    }

    // ########################################################################################
    // ###
    // ### Game logic setup
    // ###
    // ########################################################################################

    @GameComponent("gameLogicFirstWave")
    public GameLogicInterface[] firstLogicWave(
            @Inject("gravity") Gravity gravity

    ) {
        return new GameLogicInterface[]{
                gravity
        };
    }
    @GameComponent("gameLogicSecondWave")
    public GameLogicInterface[] secondLogicWave(
            @Inject("collision") Collision collision

    ) {
        return new GameLogicInterface[]{
                collision
        };
    }

    /**
     * TODO: Rename this wave to gameLogicEasyChecks?
     */
    @GameComponent("gameLogicThirdWave")
    public GameLogicInterface[] thirdLogicWave(
            @Inject("killPlayerWhenBelowScreen") KillPlayerWhenBelowScreen killer,
            @Inject("PlayerDeathListHandler") PlayerDeathListHandler playerDeathListHandler


    ) {
        return new GameLogicInterface[]{
                killer,
                playerDeathListHandler
        };
    }

    @GameComponent(GameContext.LOGIC)
    public GameLogicInterface[][] setLogicWaves(
            @Inject("gameLogicFirstWave") GameLogicInterface[] first,
            @Inject("gameLogicSecondWave") GameLogicInterface[] second,
            @Inject("gameLogicThirdWave") GameLogicInterface[] third
    ) {
        // same as GPU logic, a wave can hold N task which can run in parallel
        // but each wave is sequential

        // group waves
        return new GameLogicInterface[][]{
                first,
                second,
                third
        };
    }

    // ########################################################################################
    // ###
    // ### Game painters / renderer setup
    // ###
    // ########################################################################################
    @GameComponent(GameContext.PAINT)
    public GamePainter[][] setPaintWaves(
            @Inject("FPSPainter") GamePainter fps,
            @Inject("LPSPainter") GamePainter lps,
            @Inject("level") LevelManager level,
            @Inject("drawball") DrawBall drawball,
            @Inject("DrawHighScore") DrawHighScore drawHighScore,
            @Inject("DrawKillScreen") DrawKillScreen drawKillScreen


    ) {
        // same as GPU logic, a wave can hold N task which can run in parallel
        // but each wave is sequential

        GamePainter[] firstWave = new GamePainter[]{
                level,drawball,drawKillScreen,drawHighScore
        };

        GamePainter[] updateRatePainters = new GamePainter[] {
                fps, // frames per second
                lps  // logic rounds per second

        };

        // group waves
        return new GamePainter[][]{
                updateRatePainters,
                firstWave
                 // always last as we need these to be on top
        };
    }

    @GameComponent("gamePaintWrapper")
    public GamePaintWrapper gamePaintWrapper(
            @Inject(GameContext.PAINT) GamePainter[][] painters,
            @Inject("activity") Activity activity,
            @Inject("GameState") GameState gameState
    ) {
        return new GamePaintWrapper(activity, painters,gameState);
    }



    // ########################################################################################
    // ###
    // ### Other components
    // ###
    // ########################################################################################
    @GameComponent("gravity")
    public Gravity gravity(
            @Inject("players") Players players
    ) {
        return new Gravity(players);
    }

    @GameComponent("GameState")
    public GameState gameState(
    ) {
        return new GameState();
    }

    @GameComponent("collision")
    public Collision collision(
            @Inject("players") Players players,
            @Inject("level") LevelManager level
    ) {
        return new Collision(players,level);
    }

    @GameComponent("players")
    public Players players(
            @Inject("SensorChangedWorker") SensorChangedWorker handler
    ) {
        final Players players = new Players();
        handler.addListener(players::onSensorEvt);

        return players;
    }

    @GameComponent("canvasSize")
    public Point getCanvasSize(
            @Inject("activity") Activity activity,
            @Inject("display") Display display
    ) {
        Point canvasSize = new Point();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            // we swap these since we use landscape mode
            display.getRealSize(canvasSize);
            //this.physicalWidth = ;
            //this.physicalHeight = d.getMode().getPhysicalWidth();
        } else {
            // shit solution. doesn't really work.
            canvasSize.set(activity.getResources().getDisplayMetrics().widthPixels, activity.getResources().getDisplayMetrics().heightPixels);
        }

        return canvasSize;
    }


    @GameComponent("level")
    public LevelManager level(
            @Inject("players") Players players,
            @Inject("canvasSize") Point canvasSize
    ) {


        return new LevelManager(canvasSize.x,canvasSize.y,10,10,100,1);
    }

    @GameComponent("drawball")
    public DrawBall ball(
            @Inject("players") Players players
    ) {
        return new DrawBall(players);
    }

    @GameComponent("DrawHighScore")
    public DrawHighScore highScore(
            @Inject("players") Players players,
            @Inject("GameState") GameState gameState
    ) {
        return new DrawHighScore(players,gameState);
    }

    @GameComponent("DrawKillScreen")
    public  DrawKillScreen drawKillScreen(
            @Inject("players") Players players,
            @Inject("GameState") GameState gameState

    ) {
        return new DrawKillScreen(players,gameState);
    }
    // FPS counter / draws per second
    @GameComponent("FPS")
    public UpdateRateCounter setFPSCounter() {
        return new UpdateRateCount();
    }

    // FPS painter
    @GameComponent("FPSPainter")
    public GamePainter setFPSPainter(
            @Inject("FPS") UpdateRateCounter fps
    ) {
        UpdateRateCountPainter painter = new UpdateRateCountPainter(fps);
        painter.setPrefix("fps: ");

        return painter;
    }


    // LPS counter / logic rounds per second
    @GameComponent("LPS")
    public UpdateRateCounter setLPSCounter() {
        return new UpdateRateCount();
    }

    // LPS painter
    @GameComponent("LPSPainter")
    public GamePainter setLPSPainter(
            @Inject("LPS") UpdateRateCounter lps
    ) {
        UpdateRateCountPainter painter = new UpdateRateCountPainter(lps);
        painter.setPrefix("lps: ");
        painter.setY(110);
        painter.setHexColourCode("#4477AA");

        return painter;
    }



    // configure GameLoop counters
    @GameComponent("_register_GameLoop_rateUpdaters")
    public int setLPSAndFPSForGameLoop(
            @Inject("FPS") UpdateRateCounter fps,
            @Inject("LPS") UpdateRateCounter lps,
            @Inject("GameLoop") GameLoop gameLoop
    ) {
        gameLoop.setLps(lps);
        gameLoop.setFps(fps);

        return -1;
    }

    @GameComponent("killPlayerWhenBelowScreen")
    public KillPlayerWhenBelowScreen addDeathZone(
            @Inject("players") Players players,
            @Inject("canvasSize") Point canvasSize,
            @Inject("GameState") GameState gameState
    ) {
        return new KillPlayerWhenBelowScreen(players, canvasSize,gameState);
    }

    @GameComponent("PlayerDeathListHandler")
    public PlayerDeathListHandler playerDeathListHandler(
            @Inject("players") Players players
    ) {
        return new PlayerDeathListHandler(players);
    }
}
