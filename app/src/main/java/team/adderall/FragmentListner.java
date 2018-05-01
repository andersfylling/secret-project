package team.adderall;


import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public interface FragmentListner {
    /**
     * On get Gplay Interaction
     * @param acc
     * return void
     */
    public void onGetGplayInteraction(GoogleSignInAccount acc);

    /**
     * Start Google highscore view
     * return void
     */
    public void startGoogleHighscoreView();

    /**
     * Update player score
     * @param score
     * @return boolean didUpdate
     */
    public boolean updatePlayersScore(long score);

    /**
     * Is logged in
     * @return boolean isLoggedIn
     */
    public boolean isLoggedIn();

    /**
     * Update used language
     */
    public void askForUpdateLanguage();
}