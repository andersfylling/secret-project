package team.adderall.game;

import android.hardware.SensorEvent;
import android.view.Display;
import android.view.Surface;

import team.adderall.game.framework.component.GameComponent;
import team.adderall.game.framework.component.GameDepWire;
import team.adderall.game.framework.component.Inject;

/**
 * Stores user input or action requests before it can be processed in the game loop.
 */
@GameComponent
public class UserInputHolder
{
    private double xMovement; // movement on the x axis
    private boolean jump; // requested a jump
    private int orientation;

    @GameDepWire
    public UserInputHolder(@Inject("display") Display display)
    {
        xMovement = 0;
        jump = false;
        orientation = display.getRotation();
    }

    public void requestJump() {
        jump = true;
    }

    public void requestXAxisMovement(SensorEvent evt) {
        double x = parseXFromSensor(evt);
        this.requestXAxisMovement(x);
    }

    public void requestXAxisMovement(double distance) {
        xMovement += distance * -1; // haxor. need to get an understanding why we did this again.
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

    public boolean jumping() {
        boolean status = jump;
        jump = false;

        return status;
    }

    public double xAxisMovement() {
        double movement = xMovement;
        xMovement = 0;

        return movement;
    }
}
