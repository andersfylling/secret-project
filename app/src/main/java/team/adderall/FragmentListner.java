package team.adderall;


import android.os.Bundle;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public interface FragmentListner {
    /**
     * On get Gplay Interaction
     * @param acc
     * return void
     */
    void onGetGplayInteraction(GoogleSignInAccount acc);

    /**
     * Start Google highscore view
     * return void
     */
    void startGoogleHighscoreView();

    /**
     * Update player score
     * @param score
     * @return boolean didUpdate
     */
    boolean updatePlayersScore(long score);

    /**
     * Is logged in
     * @return boolean isLoggedIn
     */
    boolean isLoggedIn();

    /**
     * Update used language
     */
    void askForUpdateLanguage();

    /**
     * Used to get an up to date bundle with things needed to pass between fragments
     * @param bundle
     */
    void getRegisterBundleContent(Bundle bundle);
}