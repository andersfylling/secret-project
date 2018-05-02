package team.adderall.game;

import android.hardware.SensorEvent;
import android.view.Display;
import android.view.Surface;

import java.util.HashMap;
import java.util.Map;

import team.adderall.game.framework.component.GameComponent;
import team.adderall.game.framework.component.GameDepWire;
import team.adderall.game.framework.component.Inject;

/**
 * Stores user input or action requests before it can be processed in the game loop.
 */
@GameComponent
public class UserInputHolder
{
    private double[] xMovement; // movement on the x axis
    private boolean[] jump; // requested a jump

    private int orientation;
    private Players players;

    private int active;

    @GameDepWire
    public UserInputHolder(@Inject("display") Display display,
                           @Inject("players") Players p)
    {
        xMovement = new double[p.size()];
        jump = new boolean[p.size()];
        players = p;
        active = (int) players.getActive().getUserID();
        orientation = display.getRotation();
    }

    /**
     * Used explicitly for the active player.
     */
    public void requestJump() {
        jump[active] = true;
    }

    public void requestJump(int userID) {
        jump[userID] = true;
    }

    public void requestXAxisMovement(SensorEvent evt) {
        double x = parseXFromSensor(evt);
        this.requestXAxisMovement(active, x);
    }

    public void requestXAxisMovement(int userID, double distance) {
        xMovement[userID] += distance * -1; // haxor. need to get an understanding why we did this again.
    }

    /**
     * Can only be used for opponents. Do not use this logic for the main player(!).
     * @param userID
     * @param position
     */
    public void requestMPXAxisMovement(int userID, double position) {
        xMovement[userID] = position;
    }


    /**
     * Convert sensorvalues to the correct sensorvalues,
     * dependend upon the default screen rotation.
     * @param sensorEvent
     * @return sensorEvent
     */
    private double parseXFromSensor(SensorEvent sensorEvent) {
        double x = 0;

        switch (orientation)  {
            case Surface.ROTATION_0:
                x = sensorEvent.values[2];
                break;
            case Surface.ROTATION_90:
                x = sensorEvent.values[1];
                break;
            case Surface.ROTATION_180:
                x = sensorEvent.values[2] * -1;
                break;
            case Surface.ROTATION_270:
                x = sensorEvent.values[1] * -1;
                break;
        }

        return x;
    }

    public boolean jumping(int userID) {
        boolean status = jump[userID];
        jump[userID] = false;

        return status;
    }

    public double xAxisMovement(int userID) {

        double movement = xMovement[userID];
        if (userID != active && movement > 0) {
            for (Player player : players.getAlivePlayers()) {
                if (player.getUserID() == userID) {
                    movement -= player.getBallManager().getX();
                    break;
                }
            }
        }
        xMovement[userID] = 0;

        return movement;
    }
}
