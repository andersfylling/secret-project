package team.adderall.game.highscore;

import android.content.Context;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.games.Games;
import team.adderall.R;


public class GooglePlay {
    private final GoogleSignInAccount account;
    private final Context context;

    public GooglePlay(Context context, GoogleSignInAccount account){
        this.account = account;
        this.context = context;
    }

    /**
     * Update the global highscore
     * @param score
     */
    public void updatePlayersScore(long score){
        Games.getLeaderboardsClient(this.context, this.account)
                .submitScore(this.context.getString(R.string.LeaderBoard), score);

    }



}
