package team.adderall.game;

import android.content.Context;
import android.content.Intent;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.HashMap;
import java.util.Map;

import team.adderall.GameActivity;

/**
 * Populated from intent
 */
public class GameDetails {
    public final static String INTENT_KEY = "GAME_DETAILS_INTENT_KEY";
    public final static int CODE_GAME_ENDED = 34534;

    // players <userID, &Player>
    private Map<Long, Player> players;

    private long gameSeed;
    private long highscore;



    public GameDetails() {
        this.players = new HashMap<>();
        this.gameSeed = 1;
        this.highscore = 0;
    }



    public long getGameSeed() {
        return gameSeed;
    }

    public void setGameSeed(long gameSeed) {
        this.gameSeed = gameSeed;
    }

    public Map<Long, Player> getPlayers() {
        return players;
    }

    public void setPlayers(Map<Long, Player> players) {
        this.players = players;
    }

    public void addPlayer(final Player player) {
        this.players.put((long) this.players.size(), player);
    }




    public static GameDetails READ_IN_FROM_INTENT(Intent intent) {
        if (intent == null) {
            return new GameDetails();
        }

        if(!intent.hasExtra(GameDetails.INTENT_KEY)) {
            return new GameDetails();
        }

        String json = intent.getStringExtra(GameDetails.INTENT_KEY);
        GameDetails content = null;
        try{
            content = new Gson().fromJson(json, GameDetails.class);
        } catch(IllegalStateException | JsonSyntaxException e) {
            e.printStackTrace();
            content = new GameDetails();
        }

        return content;
    }

    public Intent writeToGameActivityIntent(Context ctx) {
        Intent intent = new Intent(ctx, GameActivity.class);
        return this.writeToIntent(intent);
    }

    public Intent writeToIntent(Intent intent) {
        String json = new Gson().toJson(this);
        intent.putExtra(INTENT_KEY, json);

        return intent;
    }

    public long getHighscore() {
        return highscore;
    }

    public void setHighscore(long highscore) {
        this.highscore = highscore;
    }
}
