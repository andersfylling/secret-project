package team.adderall;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;

import team.adderall.game.GameDetails;
import team.adderall.game.UserInputHolder;
import team.adderall.game.userinput.Jumping;
import team.adderall.game.userinput.SensorChangedWorker;
import team.adderall.game.framework.component.GameDepWire;
import team.adderall.game.framework.component.Inject;
import team.adderall.game.framework.configuration.GameConfiguration;
import team.adderall.game.framework.GameInitializer;
import team.adderall.game.framework.component.GameComponent;

@GameConfiguration
public class GameActivity
        extends
        Activity
        implements
        SensorEventListener
{

    private GameInitializer gameSession;
    private SensorManager sensorManager;
    private UserInputHolder userInputHolder;
    private static GameActivity self;

    private GameDetails details;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // read details from intent
        Intent intent = getIntent();
        this.details = GameDetails.READ_IN_FROM_INTENT(intent);
        if (this.details == null) {
            Intent returnIntent = new Intent();
            setResult(GameDetails.CODE_GAME_UNABLE_TO_START, returnIntent);
            finish();
            return;
        }
        self = this;

        // initialize device sensor capabilities
        this.sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        this.gameSession = new GameInitializer(
                team.adderall.game.Config.class,
                team.adderall.game.highscore.Config.class,
                team.adderall.game.ball.Config.class,
                team.adderall.game.GameExtraObjects.Config.class,
                team.adderall.game.level.Config.class
        );
        this.gameSession.loadEssentials(); // add GameContext
        this.gameSession.addGameConfigurationActivities(this); // link this instance
    }

    @Override
    public void onAttachedToWindow()
    {
        super.onAttachedToWindow();
        this.gameSession.load(()-> {
            System.out.println("######### finished loading game");
            this.gameSession.start();
        });
        System.out.println("######### loading game objects");
    }

    @GameComponent("activity")
    public Activity activity()
    {
        return this;
    }

    @GameComponent("display")
    public Display display()
    {
        final WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        if (wm == null) {
            return null;
        }

        return wm.getDefaultDisplay();
    }

    /**
     * USe this to store any details that should be returned when the game ends.
     *
     * @return
     */
    @GameComponent("GameDetails")
    public GameDetails gameDetails()
    {
        return this.details;
    }

    @GameDepWire
    public void setUserInputHolder(@Inject("userInputHolder") UserInputHolder userInputHolder)
    {
        this.userInputHolder = userInputHolder;
        this.addDeviceSensorListeners();
    }


    // TODO: move to a different class(!)
    private void addDeviceSensorListeners()
    {
        final Sensor orientation = this.sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        final int maxReportLatency = SensorManager.SENSOR_DELAY_GAME;
        this.sensorManager.registerListener(this, orientation, maxReportLatency);
    }
    private void removeDeviceSensorListeners()
    {
        this.sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent)
    {
        // when the app is closed due to connectivity issues
        // this is still triggered, somehow.
        if (userInputHolder == null) {
            return;
        }
        userInputHolder.requestXAxisMovement(sensorEvent);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i)
    {}


    /**
     * On any touch event on the screen.
     */
    @Override
    public void onUserInteraction()
    {
        super.onUserInteraction();
        this.userInputHolder.requestJump();
    }


    @Override
    public void onResume()
    {
        super.onResume();
        this.addDeviceSensorListeners();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        this.removeDeviceSensorListeners();
        //TODO: pause game loop, and unpause it in onResume()
    }

    @Override
    public void onBackPressed()
    {
        Intent returnIntent = new Intent();
        this.details.writeToIntent(returnIntent);
        this.removeDeviceSensorListeners();
        gameSession.close(); // close all @GameComponents


        setResult(GameDetails.CODE_GAME_ENDED, returnIntent);
        finish();
    }

    /**
     * TODO: change these...
     */
    public static GameActivity getActivity(){
        return self;
    }

    public void weDied() {
        onBackPressed();
    }
}
