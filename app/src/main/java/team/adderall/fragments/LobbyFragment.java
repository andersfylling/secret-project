package team.adderall.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import team.adderall.GameService;
import team.adderall.HandlePlayerStatusChanges;
import team.adderall.JSend;
import team.adderall.MainActivity;
import team.adderall.PlayerStatus;
import team.adderall.R;
import team.adderall.UserSession;

// TODO: separate view and controller
public class LobbyFragment
        extends Fragment
{
    private UserSession session;
    private View view;
    private GameService service;
    private AsyncTask statusChanges;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.lobby_view, container, false);

        Bundle extras = getArguments();
        session = new UserSession(extras);

        // build service
        // TODO: need some way to handle one shared instance
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.0.87:3173/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(GameService.class);

        statusChanges = new HandlePlayerStatusChanges(session, service, this::updatePlayerStatus);

        // button listener
        Button button = view.findViewById(R.id.button_start_lobby_game);
        button.setOnClickListener(this::joinGame);

        return view;
    }

    public void joinGame(final View v) {
        statusChanges.execute();
    }

    public void updatePlayerStatus(PlayerStatus playerStatus) {
        System.out.println(playerStatus.getMessage());
    }
}
