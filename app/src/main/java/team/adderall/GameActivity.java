package team.adderall;

import android.app.Activity;
import android.os.Bundle;

import team.adderall.game.Configuration;
import team.adderall.game.framework.GameInitializer;

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
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.gameInitializer.load(()-> {
            System.out.println("######### finished loading game");
        });
        System.out.println("######### loading game objects");
    }
}
