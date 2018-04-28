package team.adderall.fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import team.adderall.FragmentListner;
import team.adderall.R;
import team.adderall.game.highscore.GooglePlay;
import team.adderall.game.highscore.HighScoreObject;
import team.adderall.game.highscore.HighscoreAdapter;

public class ShowHighscore extends Activity {
    ArrayList<HighScoreObject> highscoreObjList;

    SharedPreferences shared;
    long CurrentScore = 0;
    private GoogleSignInAccount account;


    private void saveHighscoreList(){
        SharedPreferences.Editor editor = shared.edit();
        Gson gson = new Gson();
        String json = gson.toJson(highscoreObjList);
        editor.putString("highscore1", json);
        editor.commit();
    }

    private void getHighscoreList(){
        Gson gson = new Gson();
        String json = shared.getString("highscore1", null);
        if(json != null) {
            highscoreObjList = gson.fromJson(json, new TypeToken<List<HighScoreObject>>() {
            }.getType());
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.high_score_view);
        shared = getSharedPreferences("App_settings", MODE_PRIVATE);

        highscoreObjList = new ArrayList<>();
        getHighscoreList();

        Intent iin = getIntent();
        Bundle b = iin.getExtras();

        if (b != null) {
            CurrentScore = (long) b.get("Highscore");
            highscoreObjList.add(new HighScoreObject(CurrentScore,false));
        }

        autoSync(highscoreObjList);
        saveHighscoreList();


        Collections.sort(highscoreObjList);
        Collections.reverse(highscoreObjList);

        ListView highscoreist = findViewById(R.id.highscore);
        highscoreist.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        highscoreist.setClickable(false);

        HighscoreAdapter adp = new HighscoreAdapter(this, highscoreObjList, CurrentScore);
        highscoreist.setAdapter(adp);

        TextView result = findViewById(R.id.result);
        result.setTextSize(50);
        result.setTextColor(Color.GREEN);

        int id = adp.getIdFor(CurrentScore);
        if(id == 0) {
            result.setText("WUUT!! New Highscore. You Rock! ");
        }
        else{
            result.setText("Congrats on position " + Long.toString(id + 1));

        }

    }

    /**
     * Automatically update the global highscore list with any unsynced elements.
     * @param highscoreObjList
     */
    private void autoSync(ArrayList<HighScoreObject> highscoreObjList) {
        for(HighScoreObject obj : highscoreObjList){
            if(!obj.getSynced()){

                FragmentListner b = (FragmentListner) this;

                if(b.updatePlayersScore(obj.getScore()))
                    obj.setSynced(true);
            }
        }
    }

}
