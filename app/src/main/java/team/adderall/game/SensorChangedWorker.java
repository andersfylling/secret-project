package team.adderall.game;

import android.os.Process;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

public class SensorChangedWorker
        extends Thread
{
    private final LinkedBlockingQueue<SensorEvt> que;
    private final ArrayList<SensorEvtListener> listeners;

    private boolean notDeadYet;

    public SensorChangedWorker(final ArrayList<SensorEvtListener> listeners) {
        this.listeners = listeners;
        this.que = new LinkedBlockingQueue<>();
        this.notDeadYet = true;
    }

    public void push(SensorEvt evt) {
        this.que.offer(evt);
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

    public void kill() {
        this.notDeadYet = false;

        // add an object in case there's no more incoming events
        SensorEvt poison = new SensorEvt(0, 0);
        this.push(poison);
    }

}
