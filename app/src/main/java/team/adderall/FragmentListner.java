package team.adderall;


import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public interface FragmentListner {
    public void onGetGplayInteraction(GoogleSignInAccount acc);

    public void startGoogleHighscoreView();

    public boolean updatePlayersScore(long score);
}