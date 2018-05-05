package team.adderall.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import team.adderall.network.GameService;
import team.adderall.network.HandlePlayerStatusChanges;
import team.adderall.network.JSend;
import team.adderall.network.PlayerDetails;
import team.adderall.network.PlayerStatus;
import team.adderall.R;
import team.adderall.network.UserSession;
import team.adderall.game.GameDetails;
import team.adderall.game.Player;

/**
 * TODO: separate view and controller
 */
public class LobbyFragment
        extends
        Fragment
{
    private final static String KEY_USERNAME = "username"; // TODO: move to a static class

    private final static String MSG_CANNOT_CONNECT = "Unable to connect to game servers REST API";

    public final static boolean STAT_MULTIPLAYER = true;
    private static final int NOT_CONNECTED = -1;
    private static final int CONNECTED = 0;
    private static final int SEARCHING_FOR_LOBBY = 1;
    private static final int IN_LOBBY = 2;
    private static final int DESTROY = -2;

    private UserSession session;
    private View view;
    private GameService service;
    private AsyncTask statusChanges;
    private ListView players;
    private List<String> usernames;
    private ArrayAdapter<String> playersAdapter;
    private boolean inGame;

    private Button button;

    private int state;

    private TextView lobbyStatus;
    private ProgressBar progressBar;

    private TextView playersTitle;
    private PlayerDetails username;

    /**
     * Setup the view and try to auto authenticate with the game server.
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.lobby_view, container, false);
        inGame = false;
        SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(this.getContext());

        String key = shared.getString("settings_game_server_connection",getString(R.string.baseUrl));

        // build service
        // TODO: need some way to handle one shared instance
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(key)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(GameService.class);

        // auth to game server
        session = new UserSession();
        username = new PlayerDetails();

        Bundle extras = getArguments();
        String name = getResources().getString(R.string.username);
        if (extras != null) {
            name = extras.getString(KEY_USERNAME);
        }
        username.setUsername(name);

        // status
        lobbyStatus = view.findViewById(R.id.textView_lobby_status);
        progressBar = view.findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.GONE);

        // button listener
        button = view.findViewById(R.id.button_start_lobby_game);
        state = NOT_CONNECTED;
        updateButton();

        // list view
        playersTitle = view.findViewById(R.id.textView_players_title);
        usernames = new ArrayList<>();
        players = view.findViewById(R.id.listview_lobby_joined_players);
        playersAdapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_list_item_1, usernames);
        players.setAdapter(playersAdapter);

        // try to connect
        connect(null);

        return view;
    }

    /**
     * Connect to the game server and get a auth token.
     * @param v
     */
    private void connect(final View v) {
        progressBar.setVisibility(View.VISIBLE);
        Call<JSend<UserSession>> call = service.authenticate(username);
        final LobbyFragment self = this;
        call.enqueue(new Callback<JSend<UserSession>>() {
            @Override
            public void onResponse(Call<JSend<UserSession>> call, Response<JSend<UserSession>> response) {
                self.session.setToken(response.body().getData().getToken());
                self.lobbyStatus.setText(R.string.lobby_status_connected);
                progressBar.setVisibility(View.GONE);
                state = CONNECTED;
                updateButton();
            }

            @Override
            public void onFailure(Call<JSend<UserSession>> call, Throwable t) {
                if (self.getContext() == null) {
                    return;
                }
                Toast toast = Toast.makeText(self.getContext(), MSG_CANNOT_CONNECT, Toast.LENGTH_LONG);
                toast.show();
                self.lobbyStatus.setText(R.string.lobby_status_not_connected);
                progressBar.setVisibility(View.GONE);
                state = NOT_CONNECTED;
                updateButton();
            }
        });
    }

    /**
     * Runs a background task with long polling to stay up to date
     * with lobby changes.
     *
     * @param v
     */
    public synchronized void joinLobby(final View v) {
        if (statusChanges == null) {
            statusChanges = new HandlePlayerStatusChanges(session, service, this::updatePlayerStatus);
            statusChanges.execute();
        }
    }

    /**
     * Request to leave the lobby from the server.
     *
     * @param v
     */
    public void leaveLobby(final View v) {
        stopLongPolling();
        button.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);
        Call<JSend<PlayerStatus>> call = service.leaveLobby(session.getToken());
        final LobbyFragment self = this;
        call.enqueue(new Callback<JSend<PlayerStatus>>() {
            @Override
            public void onResponse(Call<JSend<PlayerStatus>> call, Response<JSend<PlayerStatus>> response) {
                if (state == DESTROY) {
                    return;
                }
                self.lobbyStatus.setText(R.string.lobby_status_connected);
                progressBar.setVisibility(View.GONE);
                state = CONNECTED;
                usernames.clear();
                playersAdapter.notifyDataSetChanged();
                playersTitle.setVisibility(View.GONE);
                updateButton();
            }

            @Override
            public void onFailure(Call<JSend<PlayerStatus>> call, Throwable t) {
                if (state == DESTROY) {
                    return;
                }
                // TODO: retry
                Toast toast = Toast.makeText(self.getContext(), R.string.lobby_toast_error_unable_to_leave_lobby, Toast.LENGTH_LONG);
                toast.show();
                progressBar.setVisibility(View.GONE);
                state = IN_LOBBY;
                updateButton();
            }
        });
    }

    public void lostConnection() {
        lobbyStatus.setText(R.string.lobby_status_not_connected);
        progressBar.setVisibility(View.GONE);
        state = NOT_CONNECTED;
        updateButton();
    }

    /**
     * Triggered after each long polling request about the lobby is returned.
     *
     * @param playerStatus
     */
    public void updatePlayerStatus(PlayerStatus playerStatus) {
        lobbyStatus.setText(playerStatus.getMessage());
        if (playerStatus.getSituation() > SEARCHING_FOR_LOBBY) {
            state = IN_LOBBY;
            updateButton();
            progressBar.setVisibility(View.GONE);
        }

        // TODO: don't update after first closed lobby signal
        playersTitle.setVisibility(View.VISIBLE);
        usernames.clear();
        usernames.addAll(Arrays.asList(playerStatus.getPlayersAsString()));
        playersAdapter.notifyDataSetChanged();

        String addr = playerStatus.getGameServerAddr();
        int port = playerStatus.getGameServerPort() == null ? 0 : playerStatus.getGameServerPort();
        if (!inGame && addr != null && !addr.isEmpty() && port > 0) {
            this.startGameActivity(playerStatus);
            stopLongPolling();
        }
    }

    /**
     * Used whenever you're leaving a lobby.
     * When in a lobby long polling is used to keep up to date with changes.
     */
    private void stopLongPolling() {
        if (statusChanges != null) {
            statusChanges.cancel(false);
            statusChanges = null;
        }
    }

    /**
     * Make the button state based such that he cannot
     * execute any unexpected commands/requests.
     */
    private void updateButton() {
        switch (state) {
            case NOT_CONNECTED:
                button.setText(R.string.lobby_button_connect);
                button.setOnClickListener(this::connect);
                button.setEnabled(true);
                break;
            case CONNECTED:
                button.setText(R.string.lobby_button_find_lobby);
                button.setOnClickListener(this::joinLobby);
                button.setEnabled(true);
                break;
            case IN_LOBBY:
                button.setText(R.string.lobby_button_leave_lobby);
                button.setOnClickListener(this::leaveLobby);
                button.setEnabled(true);
                break;
            default:
                button.setEnabled(false);
                break;
        }
    }

    /**
     * Lobby has closed and the game will now start.
     * @param playerStatus
     */
    public synchronized void startGameActivity(PlayerStatus playerStatus) {
        if (inGame) {
            return;
        }
        inGame = true;
        System.out.println("starting game");
        stopLongPolling();

        // game settings
        GameDetails config = new GameDetails(STAT_MULTIPLAYER);
        config.setGameSeed(playerStatus.getGameSeed());
        config.setGameID(playerStatus.getGameId());
        config.setGameServer(playerStatus.getGameServerAddr());
        config.setGameServerPort(playerStatus.getGameServerPort());

        // active player
        Player player = new Player();
        player.setActivePlayer(true);
        player.setUserID(playerStatus.getUserId());
        player.setName(username.getUsername());
        player.setGameToken(Long.parseLong(this.session.getToken()));
        config.addPlayer(player);

        // add the other players
        for (PlayerDetails gamer : playerStatus.getPlayers()) {
            if (gamer.getUserId() == player.getUserID()) {
                continue;
            }

            Player p = new Player(false);
            p.setUserID(gamer.getUserId());
            p.setName(gamer.getUsername());

            config.addPlayer(p);
        }

        Intent intent = config.writeToGameActivityIntent(getContext());
        startActivityForResult(intent, GameDetails.CODE_GAME_ENDED);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != GameDetails.CODE_GAME_ENDED){
            throw new RuntimeException("what happened?");
        }

        GameDetails result = GameDetails.READ_IN_FROM_INTENT(data);
        System.out.println(result.getHighscore());
    }


    @Override
    public void onResume()
    {
        super.onResume();
    }


    @Override
    public void onPause()
    {
        super.onPause();
        state = DESTROY;
        usernames.clear();
        playersAdapter.notifyDataSetChanged();
        stopLongPolling();
        leaveLobby(null);
    }

}
