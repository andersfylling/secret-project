package team.adderall.game;

import android.graphics.Point;
import android.view.Display;

import addy.annotations.*;
import addy.context.ServiceContext;
import addy.context.ServiceSetter;
import team.adderall.game.ball.DrawBall;
import team.adderall.game.player.PlayerDeathListHandler;
import team.adderall.game.player.Side2SideTeleportation;
import team.adderall.game.gameloop.GameLogicManager;
import team.adderall.game.gameloop.GameLoop;
import team.adderall.game.gameloop.GamePainter;
import team.adderall.game.gameloop.GraphicsManager;
import team.adderall.game.highscore.DrawHighScore;
import team.adderall.game.level.LevelManager;
import team.adderall.game.multiplayer.Multiplayer;
import team.adderall.game.physics.Collision;
import team.adderall.game.physics.DeltaTime;
import team.adderall.game.physics.Gravity;
import team.adderall.game.player.DrawKillScreen;
import team.adderall.game.player.KillPlayerWhenBelowScreen;
import team.adderall.game.player.Players;
import team.adderall.game.userinput.Jumping;
import team.adderall.game.userinput.UserInputDelegator;
import team.adderall.game.userinput.UserInputHolder;

/**
 * Reference this class in the GameActivity when initializing the game.
 */
@Configuration
@ServiceLinker({
        Players.class,
        GameState.class,
        GraphicsManager.class,
        GameLogicManager.class,
        GameLoop.class,
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
    @Service(GraphicsManager.PAINTERS_NAME)
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
    @Service("canvasSize")
    public Point getCanvasSize(@Inject("display") Display display)
    {
        Point canvasSize = new Point();
        display.getRealSize(canvasSize);

        return canvasSize;
    }

    // FPS counter / draws per second
    @Service("FPS")
    public UpdateRateCounter setFPSCounter()
    {
        return new UpdateRateCount();
    }

    // FPS painter
    @Service("FPSPainter")
    public GamePainter setFPSPainter(@Inject("FPS") UpdateRateCounter fps)
    {
        UpdateRateCountPainter painter = new UpdateRateCountPainter(fps);
        painter.setPrefix("fps: ");

        return painter;
    }


    // LPS counter / logic rounds per second
    @Service("LPS")
    public UpdateRateCounter setLPSCounter()
    {
        return new UpdateRateCount();
    }

    // LPS painter
    @Service("LPSPainter")
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
    @DepWire
    public void setLPSAndFPSForGameLoop(@Inject("FPS") UpdateRateCounter fps,
                                        @Inject("LPS") UpdateRateCounter lps,
                                        @Inject("GameLoop") GameLoop gameLoop)
    {
        gameLoop.setLps(lps);
        gameLoop.setFps(fps);
    }

    @DepWire
    public void setInitialGameSpeed(@Inject("deltaTime") DeltaTime dt)
    {
        dt.setSpeed(1.75); // TODO: use GameDetails
    }
}
