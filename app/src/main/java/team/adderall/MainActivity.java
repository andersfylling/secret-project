package team.adderall;

import android.content.ClipData;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import team.adderall.game.GameDetails;

public class MainActivity
        extends AppCompatActivity
{
    private final static Logger LOGGER = Logger.getLogger(MainActivity.class.getName());
    private DrawerLayout mDrawerLayout;
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
}
