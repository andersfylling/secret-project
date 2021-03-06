package team.adderall.game;

import android.graphics.Point;
import android.view.Display;

import team.adderall.game.ball.DrawBall;
import team.adderall.game.easyLogicChecks.PlayerDeathListHandler;
import team.adderall.game.easyLogicChecks.Side2SideTeleportation;
import team.adderall.game.framework.GameLoop;
import team.adderall.game.framework.GraphicsManager;
import team.adderall.game.framework.GamePainter;
import team.adderall.game.framework.UpdateRateCounter;
import team.adderall.game.framework.component.GameComponent;
import team.adderall.game.framework.component.GameComponents;
import team.adderall.game.framework.component.GameDepWire;
import team.adderall.game.framework.configuration.GameConfiguration;
import team.adderall.game.framework.context.GameContext;
import team.adderall.game.framework.component.Inject;
import team.adderall.game.highscore.DrawHighScore;
import team.adderall.game.level.LevelManager;
import team.adderall.game.physics.DeltaTime;
import team.adderall.game.physics.Gravity;
import team.adderall.game.userinput.Jumping;

/**
 * Reference this class in the GameActivity when initializing the game.
 */
@GameConfiguration
@GameComponents({ // initialize these from their constructor
        Players.class,
        GameState.class,
        GraphicsManager.class,
        Gravity.class,
        Collision.class,
        DrawKillScreen.class,
        KillPlayerWhenBelowScreen.class,
        PlayerDeathListHandler.class,
        Side2SideTeleportation.class,
        Multiplayer.class,
        Jumping.class,
        DeltaTime.class,
        UserInputHolder.class,
        UserInputDelegator.class,
        BlockOffTopOfScreen.class
})
public class Config
{

    // ########################################################################################
    // ###
    // ### Game painters / renderer setup
    // ###
    // ########################################################################################
    @GameComponent(GameContext.PAINT)
    public GamePainter[][] setPaintWaves(@Inject("FPSPainter") GamePainter fps,
                                         @Inject("LPSPainter") GamePainter lps,
                                         @Inject("level") LevelManager level,
                                         @Inject("drawBall") DrawBall drawball,
                                         @Inject("drawHighScore") DrawHighScore drawHighScore,
                                         @Inject("drawKillScreen") DrawKillScreen drawKillScreen)
    {
        // same as GPU logic, a wave can hold N task which can run in parallel
        // but each wave is sequential

        GamePainter[] firstWave = new GamePainter[]{
                level,
                drawball,
                drawKillScreen,
                drawHighScore
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

    // ########################################################################################
    // ###
    // ### Other components
    // ###
    // ########################################################################################
    @GameComponent("canvasSize")
    public Point getCanvasSize(@Inject("display") Display display)
    {
        Point canvasSize = new Point();
        display.getRealSize(canvasSize);

        return canvasSize;
    }

    // FPS counter / draws per second
    @GameComponent("FPS")
    public UpdateRateCounter setFPSCounter()
    {
        return new UpdateRateCount();
    }

    // FPS painter
    @GameComponent("FPSPainter")
    public GamePainter setFPSPainter(@Inject("FPS") UpdateRateCounter fps)
    {
        UpdateRateCountPainter painter = new UpdateRateCountPainter(fps);
        painter.setPrefix("fps: ");

        return painter;
    }


    // LPS counter / logic rounds per second
    @GameComponent("LPS")
    public UpdateRateCounter setLPSCounter()
    {
        return new UpdateRateCount();
    }

    // LPS painter
    @GameComponent("LPSPainter")
    public GamePainter setLPSPainter(@Inject("LPS") UpdateRateCounter lps)
    {
        UpdateRateCountPainter painter = new UpdateRateCountPainter(lps);
        painter.setPrefix("lps: ");
        painter.setY(110);
        painter.setHexColourCode("#4477AA");

        return painter;
    }

    // ########################################################################################
    // ###
    // ### Tweak GameComponents
    // ### TODO: support insert dependency injection, after construction.
    // ########################################################################################

    // configure GameLoop counters
    @GameDepWire
    public void setLPSAndFPSForGameLoop(@Inject("FPS") UpdateRateCounter fps,
                                        @Inject("LPS") UpdateRateCounter lps,
                                        @Inject("GameLoop") GameLoop gameLoop)
    {
        gameLoop.setLps(lps);
        gameLoop.setFps(fps);
    }

    @GameDepWire
    public void setInitialGameSpeed(@Inject("deltaTime") DeltaTime dt)
    {
        dt.setSpeed(1.75); // TODO: use GameDetails
    }
}
