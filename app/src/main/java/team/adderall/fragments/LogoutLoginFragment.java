package team.adderall.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import team.adderall.FragmentListner;
import team.adderall.GoogleSignInActivity;
import team.adderall.R;

public class LogoutLoginFragment
        extends Fragment
{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentListner fListner = (FragmentListner) this.getActivity();

        if(!fListner.isLoggedIn()) {
            Intent i = new Intent(this.getContext(), GoogleSignInActivity.class);
            startActivityForResult(i, 1);
        }
        else{
            fListner.onGetGplayInteraction(null);
            startMain(false);
        }

        return inflater.inflate(R.layout.logout_login_view, container, false);
    }
    private void startMain(boolean loggedIn){
        String status = loggedIn == false ? "Logged out!" : "Logged In!";
        Toast.makeText(this.getContext(),"You are now " + status,Toast.LENGTH_LONG).show();

        SoloFragment fragment = new SoloFragment();
        FragmentManager fragmentManager = this.getActivity().getSupportFragmentManager();

        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                FragmentListner b = (FragmentListner) this.getActivity();

                GoogleSignInAccount result = data.getParcelableExtra("result");



                b.onGetGplayInteraction(result);
                startMain(true);


            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }
}
