package team.adderall;

import android.content.Intent;
import android.content.res.Resources;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.games.Games;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.logging.Level;
import java.util.logging.Logger;

import team.adderall.game.highscore.GooglePlay;

public class MainActivity
        extends AppCompatActivity implements FragmentListner
{
    private final static Logger LOGGER = Logger.getLogger(MainActivity.class.getName());
    private DrawerLayout mDrawerLayout;
    private GoogleSignInAccount gplay;
    private GooglePlay gplayAcc = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new MenuItemHandler(mDrawerLayout, this));

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);

        mDrawerLayout.addDrawerListener(new MenuSlideHandler());

        LOGGER.setLevel(Level.INFO);
    }

    /**
     * Set default menu entry highlighted
     * @param menu
     * @return
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // null pointer exception
        //menu.findItem(R.id.nav_lobby).setChecked(true);

        return true;
    }


    /**
     * Show and hide side menu
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    /**
     * Exit the app on back button press.
     * Keeps the app from running in the background.
     */
    @Override
    public void onBackPressed() {
        finish();
        System.gc(); // garbage collector
        System.exit(0);
    }

    @Override
    public void onGetGplayInteraction(GoogleSignInAccount acc) {
        this.gplay = acc;
        this.gplayAcc = new GooglePlay(this,acc);

    }
    @Override
    public void startGoogleHighscoreView() {
        showLeaderboard();
    }

    @Override
    public boolean updatePlayersScore(long score) {
        if(gplayAcc != null){
            gplayAcc.updatePlayersScore(score);
            return true;
        }
        return false;
    }

    public void showLeaderboard() {
        int RC_LEADERBOARD_UI = 9004;

        Games.getLeaderboardsClient(this, this.gplay)
                .getLeaderboardIntent(getString(R.string.LeaderBoard))
                .addOnSuccessListener(new OnSuccessListener<Intent>() {
                    @Override
                    public void onSuccess(Intent intent) {
                        startActivityForResult(intent, RC_LEADERBOARD_UI);
                    }
                });
    }

}
