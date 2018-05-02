package team.adderall.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import team.adderall.R;
import team.adderall.game.GameDetails;
import team.adderall.game.Player;

public class SoloFragment
        extends Fragment
{
    public final static boolean STAT_MULTIPLAYER = false;
    private String username="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.solo_view, container, false);

        //Get username
        this.username = getArguments() == null ? this.getContext().getString(R.string.username) :
                getArguments().getString("username",
                this.getContext().getString(R.string.username));
        //this.username = savedInstanceState == null ?
        //        this.getContext().getString(R.string.username)
        //        : savedInstanceState.getString(username);

        Button button = view.findViewById(R.id.button_start_solo_game);
        button.setOnClickListener(this::startGameActivity);

        return view;
    }

    public void startGameActivity(final View view) {
        GameDetails config = new GameDetails(STAT_MULTIPLAYER);
        config.setGameSeed(542352);

        Player player = new Player();
        player.setActivePlayer(true);

        player.setName(this.username);
        config.addPlayer(player);

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

        HighScoreFragment fragment = new HighScoreFragment();
        android.app.FragmentManager fragmentManager = this.getActivity().getFragmentManager();

        Bundle bundle = new Bundle();
        bundle.putLong("Highscore", result.getHighscore());
        fragment.setArguments(bundle);

        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
    }
}
