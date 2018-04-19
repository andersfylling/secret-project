package team.adderall;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import team.adderall.fragments.HighScoreFragment;
import team.adderall.fragments.LobbyFragment;
import team.adderall.fragments.LogoutLoginFragment;
import team.adderall.fragments.SettingsFragment;
import team.adderall.fragments.SoloFragment;

public class MenuItemHandler
        implements NavigationView.OnNavigationItemSelectedListener
{
    private DrawerLayout mDrawerLayout;
    private AppCompatActivity activity;
    private final String appName;

    public MenuItemHandler(DrawerLayout mDrawerLayout, AppCompatActivity activity) {
        this.mDrawerLayout = mDrawerLayout;
        this.activity = activity;
        this.appName = activity.getString(R.string.app_name);
    }

    /**
     * Called when an item in the navigation menu is selected.
     *
     * @param item The selected item
     * @return true to display the item as the selected item
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // set item as selected to persist highlight
        item.setChecked(true);
        // close drawer when item is tapped
        mDrawerLayout.closeDrawers();

        Fragment fragment = null;
        Class fragmentClass;

        // select correct fragment
        switch (item.getItemId()) {
            case R.id.nav_lobby:
                fragmentClass = LobbyFragment.class;
                break;
            case R.id.nav_solo:
                fragmentClass = SoloFragment.class;
                break;
            case R.id.nav_high_score:
                fragmentClass = HighScoreFragment.class;
                break;
            case R.id.nav_settings:
                fragmentClass = SettingsFragment.class;
                break;
            case R.id.nav_logout_login:
                fragmentClass = LogoutLoginFragment.class;
                break;

            default:
                fragmentClass = LobbyFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        // Set action bar title
        activity.setTitle(appName + ": " + item.getTitle());

        // continue
        return true;
    }
}
