package team.adderall;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.games.Games;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Locale;
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

    private MenuItemHandler menuItemHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateLanguage();
        setContentView(R.layout.activity_main);

        mDrawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        menuItemHandler = new MenuItemHandler(mDrawerLayout, this, this::registerBundleContent);
        navigationView.setNavigationItemSelectedListener(menuItemHandler);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);

        mDrawerLayout.addDrawerListener(new MenuSlideHandler());

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            this.gplay = bundle.getParcelable("gplay");
        }

        if(this.gplay == null) {
            updateMenuToSignedIn(false);
        }
        else {
            updateMenuToSignedIn(true);
            this.gplayAcc = new GooglePlay(this, this.gplay);

        }

        LOGGER.setLevel(Level.INFO);
    }


    // called whenever a new fragment is started
    private void registerBundleContent(Bundle bundle) {
        String name;

        if (gplay == null) {
            NavigationView nv = findViewById(R.id.nav_view);
            TextView username = nv.getHeaderView(0).findViewById(R.id.headerName);
            name = username.getText().toString();
        } else {
            name = gplay.getDisplayName();
        }
        bundle.putString("username", name);
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
    public void getRegisterBundleContent(Bundle bundle){
        registerBundleContent(bundle);
    }

    @Override
    public void onGetGplayInteraction(GoogleSignInAccount acc) {
        this.gplay = acc;
        this.gplayAcc = new GooglePlay(this,acc);
        if(acc != null) {
            updateMenuToSignedIn(true);
            updateUsername(acc.getDisplayName());
        }
        else {
            this.gplayAcc = null;
            updateMenuToSignedIn(false);
            updateUsername("");

        }
    }

    private void updateUsername(String displayName) {
        NavigationView nv = findViewById(R.id.nav_view);
        TextView username = nv.getHeaderView(0).findViewById(R.id.headerName);
        username.setText(displayName);

    }

    private void updateMenuToSignedIn(boolean b) {
        String title = b ?  getString(R.string.logout) : getString(R.string.login);
        NavigationView nv = findViewById(R.id.nav_view);
        MenuItem login =  nv.getMenu().getItem(4);
        login.setTitle(title);
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

    @Override
    public boolean isLoggedIn() {
        return isGplayLoggedIn();
    }

    @Override
    public void askForUpdateLanguage(){
        updateLanguage();
        this.restart();

    }
    /**
     * Read in  language from shared pref
     * and then set the language.
     */
    public void updateLanguage() {
        Resources res = this.getApplicationContext().getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(this);

        String language = shared.getString("languagekey","default");
        if(!language.equals("default")) {
            conf.setLocale(new Locale(language));
            res.updateConfiguration(conf, dm);
        }

    }

    /**
     * Restart MainActivity
     */
    private void restart() {

        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        Bundle mBundle = new Bundle();
        onSaveInstanceState(mBundle);
        intent.putExtras(mBundle);

        startActivity(intent);
        finish();
    }

    /**
     * Show leaderboard
     * return void
     */
    public void showLeaderboard() {
        int RC_LEADERBOARD_UI = 9004;

        if(!isGplayLoggedIn()){
            Toast.makeText(this.getApplicationContext(),
                    R.string.needToLogIn, Toast.LENGTH_SHORT).show();
            return;
        }

        Games.getLeaderboardsClient(this, this.gplay)
                .getLeaderboardIntent(getString(R.string.LeaderBoard))
                .addOnSuccessListener(intent -> startActivityForResult(intent, RC_LEADERBOARD_UI));
    }

    /**
     * Is gplay logged in
     * @return booled isLoggedIn
     */
    public boolean isGplayLoggedIn() {
        return (gplayAcc != null);
    }


    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putParcelable("gplay", this.gplay);
    }

}
