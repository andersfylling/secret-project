package team.adderall;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.WindowManager;

import team.adderall.game.Configuration;
import team.adderall.game.framework.configuration.GameConfiguration;
import team.adderall.game.framework.GameInitializer;
import team.adderall.game.framework.component.GameComponent;

@GameConfiguration
public class GameActivity
        extends Activity
{

    private GameInitializer gameInitializer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

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
}
