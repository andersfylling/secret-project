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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.games.Games;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.logging.Level;
import java.util.logging.Logger;

import team.adderall.game.highscore.GooglePlay;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import team.adderall.network.GameService;
import team.adderall.network.JSend;
import team.adderall.network.PlayerDetails;
import team.adderall.network.UserSession;

public class MainActivity
        extends AppCompatActivity implements FragmentListner
{
    private final static Logger LOGGER = Logger.getLogger(MainActivity.class.getName());
    private DrawerLayout mDrawerLayout;
    private GoogleSignInAccount gplay;
    private GooglePlay gplayAcc = null;

    private GameService service;
    private UserSession session;
    private MenuItemHandler menuItemHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.0.87:3173/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(GameService.class);

        // auth to game server
        session = new UserSession();
        PlayerDetails username = new PlayerDetails();
        username.setUsername("andemann"); // TODO: get from settings
        Call<JSend<UserSession>> call = service.authenticate(username);
        final MainActivity self = this;
        call.enqueue(new Callback<JSend<UserSession>>() {
            @Override
            public void onResponse(Call<JSend<UserSession>> call, Response<JSend<UserSession>> response) {
                self.session.setToken(response.body().getData().getToken());

                NavigationView navigationView = findViewById(R.id.nav_view);

                Menu menuNav = navigationView.getMenu();
                MenuItem navLobby = menuNav.findItem(R.id.nav_lobby);
                navLobby.setEnabled(true);
            }

            @Override
            public void onFailure(Call<JSend<UserSession>> call, Throwable t) {
                // TODO: retry
                Toast toast = Toast.makeText(self, "Unable to connect to game servers REST API", Toast.LENGTH_LONG);
                toast.show();
            }
        });

        updateMenuToSignedIn(false);

        LOGGER.setLevel(Level.INFO);
    }

    // called whenever a new fragment is started
    private void registerBundleContent(Bundle bundle) {
        bundle.putString(UserSession.SESSION_TOKEN_NAME, this.session.getToken());
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
        if(acc != null) {
            updateMenuToSignedIn(true);
            updateUsername(acc.getDisplayName());
        }
        else{
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
        String title = b == true?  "Logout" : "Login";
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

    public void showLeaderboard() {
        int RC_LEADERBOARD_UI = 9004;

        if(!isGplayLoggedIn()){
            Toast.makeText(this.getApplicationContext(),
                    "You need to be logged in to use this action", Toast.LENGTH_SHORT).show();
            return;
        }

        Games.getLeaderboardsClient(this, this.gplay)
                .getLeaderboardIntent(getString(R.string.LeaderBoard))
                .addOnSuccessListener(new OnSuccessListener<Intent>() {
                    @Override
                    public void onSuccess(Intent intent) {
                        startActivityForResult(intent, RC_LEADERBOARD_UI);
                    }
                });
    }

    public boolean isGplayLoggedIn() {
        return (gplayAcc != null);
    }
}
