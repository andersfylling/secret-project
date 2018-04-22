package team.adderall;

import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.view.View;

public class MenuSlideHandler
    implements DrawerLayout.DrawerListener
{
    /**
     * Called when a drawer's position changes.
     *
     * @param drawerView  The child view that was moved
     * @param slideOffset The new offset of this drawer within its range, from 0-1
     */
    @Override
    public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

    }

    /**
     * Called when a drawer has settled in a completely open state.
     * The drawer is interactive at this point.
     *
     * @param drawerView Drawer view that is now open
     */
    @Override
    public void onDrawerOpened(@NonNull View drawerView) {

    }

    /**
     * Called when a drawer has settled in a completely closed state.
     *
     * @param drawerView Drawer view that is now closed
     */
    @Override
    public void onDrawerClosed(@NonNull View drawerView) {

    }

    /**
     * Called when the drawer motion state changes. The new state will
     * be one of {@link #STATE_IDLE}, {@link #STATE_DRAGGING} or {@link #STATE_SETTLING}.
     *
     * @param newState The new drawer motion state
     */
    @Override
    public void onDrawerStateChanged(int newState) {

    }
}
