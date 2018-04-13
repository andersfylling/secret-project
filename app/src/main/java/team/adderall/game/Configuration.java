package team.adderall.game;

import android.app.Activity;

import team.adderall.game.framework.GamePaintWrapper;
import team.adderall.game.framework.GamePainter;
import team.adderall.game.framework.UpdateRateCounter;
import team.adderall.game.framework.component.GameComponent;
import team.adderall.game.framework.configuration.GameConfiguration;
import team.adderall.game.framework.context.GameContext;
import team.adderall.game.framework.context.GameContextGetterAssured;
import team.adderall.game.framework.context.GameContextSetter;
import team.adderall.game.framework.GameLogicInterface;
import team.adderall.game.framework.component.GameComponentRegister;
import team.adderall.game.framework.component.Inject;

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

    @GameComponent(GameContext.LOGIC)
    public GameLogicInterface[][] setLogicWaves(
            @Inject("gameLogicFirstWave") GameLogicInterface[] first
    ) {
        // same as GPU logic, a wave can hold N task which can run in parallel
        // but each wave is sequential

        // group waves
        return new GameLogicInterface[][]{
                first
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
            @Inject("LPSPainter") GamePainter lps
    ) {
        // same as GPU logic, a wave can hold N task which can run in parallel
        // but each wave is sequential

        GamePainter[] firstWave = new GamePainter[]{
        };

        GamePainter[] updateRatePainters = new GamePainter[] {
                fps, // frames per second
                lps  // logic rounds per second
        };

        // group waves
        return new GamePainter[][]{
                firstWave,

                updateRatePainters // always last as we need these to be on top
        };
    }

    @GameComponent("gamePaintWrapper")
    public GamePaintWrapper gamePaintWrapper(
            @Inject(GameContext.PAINT) GamePainter[][] painters,
            @Inject("activity") Activity activity
    ) {
        return new GamePaintWrapper(activity, painters);
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

    @GameComponent("players")
    public Players players() {
        return new Players();
    }

    // FPS counter / draws per second
    @GameComponent("FPS")
    public UpdateRateCounter setFPSCounter() {
        return new UpdateRateCount();
    }

    // FPS painter
    @GameComponent("FPSPainter")
    public GamePainter setFPSPainter(@Inject("FPS") UpdateRateCounter fps) {
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
    public GamePainter setLPSPainter(@Inject("LPS") UpdateRateCounter lps) {
        UpdateRateCountPainter painter = new UpdateRateCountPainter(lps);
        painter.setPrefix("lps: ");
        painter.setY(110);
        painter.setHexColourCode("#4477AA");

        return painter;
    }
}
