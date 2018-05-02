package team.adderall.game;


import android.graphics.Rect;


/**
 * Anyone that crosses this border dies.
 * Typically at the bottom of the screen.
 */
@Deprecated // old solution, useful if we need more advanced checking than just below a perfectly horizontal line.
public class DeathZone
{
    private final Rect zone;

    // must be below this line in order to do a check
    // perf improvement
    //
    // mobile phone (screen)
    // ########### <- top of mobile device
    // #         #
    // #         #
    // #    X    # <- this ball survives / don't check if inside death zone
    // #         #
    // #         #
    // #---------# <- yThreshold line, anything above this and we ignore checking.
    // #         #
    // #     X   # <- this ball is below yThreshold, check if within death zone
    // #*********# <- death zone
    // #*X*******# <- this ball is below yThreshold, check if within death zone, it is => dead
    // ###########
    // ########### <- bottom of mobile device
    //
    private int yThreshold;

    public DeathZone(final Rect zone) {
        this.zone = zone;
        this.yThreshold = -500; // a little above the screen
    }

    public void setYThreshold(final int y) {
        this.yThreshold = y;
    }

    /**
     * Check if a point is beyond the border AKA should be killed.
     * @param x
     * @param y
     * @return
     */
    public boolean touchedDeath(final int x, final int y) {
        return this.yThreshold < y && this.zone.contains(x, y);
    }
}