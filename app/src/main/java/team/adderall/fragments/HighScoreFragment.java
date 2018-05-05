package team.adderall.fragments;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Style;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import team.adderall.FragmentListner;
import team.adderall.R;
import team.adderall.game.highscore.HighScoreLogEntry;
import team.adderall.game.highscore.HighscoreAdapter;

import static android.content.Context.MODE_PRIVATE;

public class HighScoreFragment
        extends Fragment
{

    private ArrayList<HighScoreLogEntry> highscoreObjList;
    private HighscoreAdapter adp;

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
            highscoreObjList.add(new HighScoreLogEntry(CurrentScore, false));
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

        adp = new HighscoreAdapter(this.getContext(), highscoreObjList, CurrentScore);
        highscoreist.setAdapter(adp);

        TextView result = view.findViewById(R.id.result);
        result.setTextSize(50);
        result.setTextColor(Color.GREEN);

        int id = adp.getIdFor(CurrentScore);
        switch (id) {
            case 0:
                newBestHighScoreAction();
                break;
            case -1:
                result.setVisibility(View.INVISIBLE);
                break;
            default:
                newHighScoreAction();
        }

        return view;
    }

    private void newHighScoreAction() {
        int id = adp.getIdFor(CurrentScore);
        MaterialStyledDialog dialog = new MaterialStyledDialog.Builder(getContext())
                .setTitle(R.string.highscore_congrats)
                .setDescription(getString(R.string.newNotHighscore,Long.toString(id + 1)) + "\n\n")
                .setStyle(Style.HEADER_WITH_TITLE)
                .setHeaderColor(R.color.colorPrimary)
                .withDialogAnimation(true)
                .build();

        dialog.show();
    }
    private void newBestHighScoreAction() {
        MaterialStyledDialog dialog = new MaterialStyledDialog.Builder(getContext())
                .setTitle(R.string.highscore_congrats_best)
                .setDescription(getString(R.string.newHighscore) + "\n\n")
                .setStyle(Style.HEADER_WITH_TITLE)
                .setHeaderColor(R.color.md_material_blue_800)
                .withDialogAnimation(true)
                .build();

        dialog.show();
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
        editor.apply();
    }

    /**
     * Get highscore list from shared
     */
    private void getHighscoreList(){
        Gson gson = new Gson();
        String json = shared.getString("highscore1", null);
        if(json != null) {
            highscoreObjList = gson.fromJson(json, new TypeToken<List<HighScoreLogEntry>>() {
            }.getType());
        }
    }


    /**
     * Automatically update the global highscore list with any unsynced elements.
     * @param highscoreObjList
     */
    private void autoSync(ArrayList<HighScoreLogEntry> highscoreObjList) {

        FragmentListner fListner = (FragmentListner) this.getActivity();

        if(fListner.isLoggedIn()) {

            for (HighScoreLogEntry obj : highscoreObjList) {
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
