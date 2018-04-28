package team.adderall;

import android.app.Activity;

import org.junit.Test;

import team.adderall.game.framework.GameInitializer;
import team.adderall.game.framework.configuration.GameConfiguration;

@GameConfiguration
public class GameActivityTest extends Activity {

    @Test
    public void testCheckIfGameCanBeLoaded() {
        // TODO: find a way to mock create the GameActivity

//        GameActivity gameActivity = new GameActivity();
//        gameActivity.onAttachedToWindow();
    }
}
