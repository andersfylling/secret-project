package team.adderall.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import team.adderall.FragmentListner;
import team.adderall.GoogleSignInActivity;
import team.adderall.R;
import team.adderall.game.highscore.UserP;

public class LogoutLoginFragment
        extends Fragment
{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Intent i = new Intent(this.getContext(), GoogleSignInActivity.class);
        startActivityForResult(i, 1);

        return inflater.inflate(R.layout.logout_login_view, container, false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                FragmentListner b = (FragmentListner) this.getActivity();

                GoogleSignInAccount result= data.getParcelableExtra("result");



                b.onGetGplayInteraction(result);

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }//onActivityResult
}
