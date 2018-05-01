package team.adderall.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import team.adderall.FragmentListner;
import team.adderall.R;
import team.adderall.game.highscore.HighScoreObject;
import team.adderall.game.highscore.HighscoreAdapter;

import static android.content.Context.MODE_PRIVATE;

public class HighScoreFragment
        extends Fragment
{

    ArrayList<HighScoreObject> highscoreObjList;

    SharedPreferences shared;
    long CurrentScore = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.high_score_view, container, false);


        shared = this.getActivity().getSharedPreferences("App_settings", MODE_PRIVATE);

        highscoreObjList = new ArrayList<>();
        getHighscoreList();

        CurrentScore = getArguments().getLong("Highscore",-1);
        if(CurrentScore != -1) {
            highscoreObjList.add(new HighScoreObject(CurrentScore, false));
        }

        autoSync(highscoreObjList);
        saveHighscoreList();

        Collections.sort(highscoreObjList);
        Collections.reverse(highscoreObjList);

        ListView highscoreist = view.findViewById(R.id.highscore);
        highscoreist.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        highscoreist.setClickable(false);

        Button showHighscoreList = view.findViewById(R.id.showHH);
        showHighscoreList.setOnClickListener(e->openGooglePlayHighscoreList());

        HighscoreAdapter adp = new HighscoreAdapter(this.getContext(), highscoreObjList, CurrentScore);
        highscoreist.setAdapter(adp);

        TextView result = view.findViewById(R.id.result);
        result.setTextSize(50);
        result.setTextColor(Color.GREEN);

        int id = adp.getIdFor(CurrentScore);
        if(id == 0) {
            result.setText(R.string.newHighscore);
        }
        else if(id == -1){
            result.setVisibility(View.INVISIBLE);
        }
        else{
            result.setText(getString(R.string.newNotHighscore,Long.toString(id + 1)));

        }

        return view;
    }

    /**
     * Open the global highscore list
     */
    private void openGooglePlayHighscoreList() {
        FragmentListner fListner = (FragmentListner) this.getActivity();
        fListner.startGoogleHighscoreView();
    }


    /**
     * Save highscore list to sharedpreferences
     */
    private void saveHighscoreList(){
        SharedPreferences.Editor editor = shared.edit();
        Gson gson = new Gson();
        String json = gson.toJson(highscoreObjList);
        editor.putString("highscore1", json);
        editor.commit();
    }

    /**
     * Get highscore list from shared
     */
    private void getHighscoreList(){
        Gson gson = new Gson();
        String json = shared.getString("highscore1", null);
        if(json != null) {
            highscoreObjList = gson.fromJson(json, new TypeToken<List<HighScoreObject>>() {
            }.getType());
        }
    }


    /**
     * Automatically update the global highscore list with any unsynced elements.
     * @param highscoreObjList
     */
    private void autoSync(ArrayList<HighScoreObject> highscoreObjList) {

        FragmentListner fListner = (FragmentListner) this.getActivity();

        if(fListner.isLoggedIn()) {

            for (HighScoreObject obj : highscoreObjList) {
                if (!obj.getSynced()) {

                    if (fListner.updatePlayersScore(obj.getScore()))
                        obj.setSynced(true);
                }
            }
        }
        else{
            Toast.makeText(this.getContext(),
                    R.string.ToAutoSyncEnable,Toast.LENGTH_LONG).show();
        }

    }


}
