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

import team.adderall.game.Configuration;
import team.adderall.game.GameDetails;
import team.adderall.game.Jumping;
import team.adderall.game.Player;
import team.adderall.game.Players;
import team.adderall.game.SensorChangedWorker;
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

    private GameInitializer gameInitializer;
    private SensorChangedWorker sensorChangedWorker;
    private SensorManager sensorManager;
    private Jumping jumping;

    private GameDetails details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // read details from intent
        Intent intent = getIntent();
        this.details = GameDetails.READ_IN_FROM_INTENT(intent);


        // initialize device sensor capabilities
        this.sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        this.addDeviceSensorListeners();

        this.gameInitializer = new GameInitializer(
                Configuration.class
        );
        this.gameInitializer.loadEssentials(); // add GameContext
        this.gameInitializer.addGameConfigurationActivities(this); // link this instance
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.gameInitializer.load(()-> {
            System.out.println("######### finished loading game");
            this.gameInitializer.start();
        });
        System.out.println("######### loading game objects");
    }


    @GameComponent("SensorChangedWorker")
    public SensorChangedWorker setSensorChangedWorker(
            @Inject("display") Display display
    ) {
        // use display to get screen orientation to manipulate x,z,y changes on sensor events
        this.sensorChangedWorker = new SensorChangedWorker(display);
        this.sensorChangedWorker.start(); // start thread

        return this.sensorChangedWorker;
    }

    @GameComponent("jumping")
    public Jumping setJumpListener(
            @Inject("players") Players players
    ) {
        this.jumping = new Jumping(players);
        return this.jumping;
    }

    @GameComponent("activity")
    public Activity activity() {
        return this;
    }

    @GameComponent("display")
    public Display display() {
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
    public GameDetails gameDetails() {
        return this.details;
    }

    @GameComponent("players")
    public Players players() {
        final Players players = new Players();
        players.registerPlayersWithUserID(this.details.getPlayers());
        for(Player player : players.getAlivePlayersAsList()) {
            player.createBallManager(player.isActivePlayer());
        }

        return players;
    }

    // TODO: move to a different class(!)
    private void addDeviceSensorListeners() {
        if (this.sensorManager == null) {
            return;
        }

        final Sensor orientation = this.sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        final int maxReportLatency = SensorManager.SENSOR_DELAY_GAME;
        this.sensorManager.registerListener(this, orientation, maxReportLatency);
    }
    private void removeDeviceSensorListeners() {
        if (this.sensorManager == null) {
            return;
        }

        this.sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (this.sensorChangedWorker == null) {
            return;
        }

        this.sensorChangedWorker.push(sensorEvent);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {}


    /**
     * On any touch event on the screen.
     */
    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        this.jumping.run();
    }


    @Override
    public void onResume(){
        super.onResume();
        this.addDeviceSensorListeners();
    }

    @Override
    public void onPause(){
        super.onPause();
        this.removeDeviceSensorListeners();
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        this.details.writeToIntent(returnIntent);

        setResult(GameDetails.CODE_GAME_ENDED, returnIntent);
        finish();
    }
}
