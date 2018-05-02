package team.adderall.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;

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
            startMain(false,null);
        }

        return inflater.inflate(R.layout.logout_login_view, container, false);
    }
    private void startMain(boolean loggedIn,Bundle bundle){
        String status = loggedIn == false ? getString(R.string.logged_out) : getString(R.string.logged_in);
        Toast.makeText(this.getContext(),this.getContext().getString(R.string.you_are_now,status),Toast.LENGTH_LONG).show();

        SoloFragment fragment = new SoloFragment();
        android.app.FragmentManager fragmentManager = this.getActivity().getFragmentManager();
        fragment.setArguments(bundle);

        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                FragmentListner b = (FragmentListner) this.getActivity();

                GoogleSignInAccount result = data.getParcelableExtra("result");



                b.onGetGplayInteraction(result);
                Bundle bundle = new Bundle();

                b.getregisterBundleContent(bundle);
                startMain(true, bundle);


            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }
}
