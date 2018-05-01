package team.adderall.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import team.adderall.network.GameService;
import team.adderall.network.HandlePlayerStatusChanges;
import team.adderall.network.PlayerDetails;
import team.adderall.network.PlayerStatus;
import team.adderall.R;
import team.adderall.network.UserSession;
import team.adderall.game.GameDetails;
import team.adderall.game.Player;

// TODO: separate view and controller
public class LobbyFragment
        extends Fragment
{
    public final static boolean STAT_MULTIPLAYER = true;

    private UserSession session;
    private View view;
    private GameService service;
    private AsyncTask statusChanges;
    private GridView players;
    private List<String> usernames;
    private ArrayAdapter<String> playersAdapter;
    private boolean inGame;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.lobby_view, container, false);

        Bundle extras = getArguments();
        session = new UserSession(extras);
        inGame = false;

        // build service
        // TODO: need some way to handle one shared instance
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.baseUrl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(GameService.class);

        statusChanges = new HandlePlayerStatusChanges(session, service, this::updatePlayerStatus);

        // button listener
        Button button = view.findViewById(R.id.button_start_lobby_game);
        button.setOnClickListener(this::joinGame);

        // grid view
        usernames = new ArrayList<>();
        players = view.findViewById(R.id.gridview_lobby_joined_players);
        playersAdapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_list_item_1, usernames);
        players.setAdapter(playersAdapter);

        return view;
    }

    public void joinGame(final View v) {
        statusChanges.execute();
    }

    public void updatePlayerStatus(PlayerStatus playerStatus) {
        System.out.println(playerStatus.getMessage());

        // TODO: don't update after first closed lobby signal
        usernames.clear();
        usernames.addAll(Arrays.asList(playerStatus.getPlayersAsString()));
        playersAdapter.notifyDataSetChanged();

        String addr = playerStatus.getGameServerAddr();
        int port = playerStatus.getGameServerPort() == null ? 0 : playerStatus.getGameServerPort();
        if (!inGame && addr != null && !addr.isEmpty() && port > 0) {
            this.startGameActivity(playerStatus);
            statusChanges.cancel(false);
        }
    }

    public synchronized void startGameActivity(PlayerStatus playerStatus) {
        if (inGame) {
            return;
        }
        inGame = true;

        GameDetails config = new GameDetails(STAT_MULTIPLAYER);
        config.setGameSeed(542352);
        //config.setGameSeed(playerStatus.getGameSeed);
        config.setGameID(playerStatus.getGameId());
        config.setGameServer(playerStatus.getGameServerAddr());
        config.setGameServerPort(playerStatus.getGameServerPort());

        Player player = new Player();
        player.setActivePlayer(true);
        player.setUserID(playerStatus.getUserId());
        player.setGameToken(Long.parseLong(this.session.getToken()));
        config.addPlayer(player);

        // add the other players
        for (int i = 0; i < playerStatus.getPlayers().size(); i++) {
            PlayerDetails pd = playerStatus.getPlayers().get(i);
            if (pd == null || i == playerStatus.getUserId()) {
                continue;
            }

            Player p = new Player();
            p.setUserID(i); // TODO: fix as this is not correct in case more than 2 users exist
            config.addPlayer(p);
        }

        Intent intent = config.writeToGameActivityIntent(getContext());
        startActivityForResult(intent, GameDetails.CODE_GAME_ENDED);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != GameDetails.CODE_GAME_ENDED){
            throw new RuntimeException("omg wtf happened?");
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
        usernames.clear();
        playersAdapter.notifyDataSetChanged();
        statusChanges.cancel(false);
    }

}
