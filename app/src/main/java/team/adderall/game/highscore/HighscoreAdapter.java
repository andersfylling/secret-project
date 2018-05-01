package team.adderall.game.highscore;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import team.adderall.R;


public class HighscoreAdapter extends BaseAdapter {
    private final long currentScore;
    private ArrayList<Long> array;
    private Context context;

    public HighscoreAdapter(Context context, ArrayList<HighScoreObject> highscoreObjList, long currentScore) {
        this.context = context;
        this.currentScore = currentScore;

        array = new ArrayList<>();
        for(HighScoreObject obj :highscoreObjList) {
            if (array.size() == 0)
                array.add(obj.getScore());
            if (array.get(array.size() - 1) != obj.getScore())
                array.add(obj.getScore());
        }

    }

    @Override
    public int getCount() {
        return this.array.size();
    }

    @Override
    public Object getItem(int position) {
        return this.array.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }




    public int getIdFor(long checkscore){
        return array.indexOf(checkscore);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View newView;

        if (convertView == null) {
            newView = View.inflate(context, R.layout.highscoreitem, null);
        } else {
            newView = convertView;
        }
        newView.setBackgroundColor(Color.WHITE);

        if(getIdFor(currentScore) == position){
            newView.setBackgroundColor(Color.BLUE);
        }
        String txt =  Long.toString((long)this.getItem(position));

        TextView view = newView.findViewById(R.id.highscore_number);

        view.setText("Position: "
                + (position + 1) + ") ");

        ((TextView) newView.findViewById(R.id.highscore_score)).setText("Score: "+ txt);

        return newView;
    }

}