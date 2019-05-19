package team.adderall.game.userinput;

import android.hardware.SensorEvent;
import android.os.Process;
import android.view.Display;
import android.view.Surface;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import addy.annotations.*;


@Deprecated
@Service("SensorChangedWorker")
public class SensorChangedWorker
        extends Thread
{
    private final LinkedBlockingQueue<SensorEvt> que;
    private final List<SensorEvtListener> listeners;

    private boolean notDeadYet;
    private int orientation;

    @DepWire
    public SensorChangedWorker(
            @Inject("display") Display display
    ) {
        this.listeners = new ArrayList<>();
        this.que = new LinkedBlockingQueue<>();
        this.notDeadYet = true;
        this.orientation = display.getOrientation();
    }

    public void push(SensorEvent sensorEvent) {
        SensorEvent processed = getCorrectSensorValues(sensorEvent);
        SensorEvt evt = new SensorEvt(
                processed.values[1], // x
                processed.values[2]  // y
        );
        this.push(evt);
    }

    public void push(SensorEvt evt) {
        this.que.offer(evt);
    }

    /**
     * Convert sensorvalues to the correct sensorvalues,
     * dependend upon the default screen rotation.
     * @param sensorEvent
     * @return sensorEvent
     */
    private SensorEvent getCorrectSensorValues(SensorEvent sensorEvent) {
        float x=0;
        float y=0;

        switch (this.orientation)  {
            case Surface.ROTATION_0:
                x=sensorEvent.values[2];
                y=sensorEvent.values[1];
                break;
            case Surface.ROTATION_90:
                x=sensorEvent.values[1];
                y=-sensorEvent.values[2];
                break;
            case Surface.ROTATION_180:
                x=-sensorEvent.values[2];
                y=-sensorEvent.values[1];
                break;
            case Surface.ROTATION_270:
                x=-sensorEvent.values[1];
                y=sensorEvent.values[2];
                break;
        }
        sensorEvent.values[1] = x;
        sensorEvent.values[2] = -y;

        return sensorEvent;
    }

    public void addListener(final SensorEvtListener listener) {
        this.listeners.add(listener);
    }

    @Override
    public void run() {
        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

        while (this.notDeadYet) {
            try {
                SensorEvt evt = this.que.take();

                for (SensorEvtListener listener : this.listeners) {
                    listener.onSensorEvt(evt);
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void close() {
        this.notDeadYet = false;

        // add an object in case there's no more incoming events
        SensorEvt poison = new SensorEvt(0, 0);
        this.push(poison);
    }

}
