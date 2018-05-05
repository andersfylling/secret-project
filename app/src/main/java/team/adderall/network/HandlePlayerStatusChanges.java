package team.adderall.network;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HandlePlayerStatusChanges
        extends
        AsyncTask
        implements
        Callback<JSend<PlayerStatus>>
{
    private final static long DEFAULT_TIMEOUT = 900;

    private UserSession session;
    private GameService service;
    private NotifyWhenPlayerStatusChanges listener;

    public HandlePlayerStatusChanges(@NonNull UserSession session,
                                     @NonNull GameService service,
                                     @NonNull NotifyWhenPlayerStatusChanges listener)
    {
        super();
        this.session = session;
        this.service = service;
        this.listener = listener;
    }

    /**
     * Override this method to perform a computation on a background thread. The
     * specified parameters are the parameters passed to {@link #execute}
     * by the caller of this task.
     * <p>
     * This method can call {@link #publishProgress} to publish updates
     * on the UI thread.
     *
     * @param objects The parameters of the task.
     * @return A result, defined by the subclass of this task.
     * @see #onPreExecute()
     * @see #onPostExecute
     * @see #publishProgress
     */
    @Override
    protected Object doInBackground(Object[] objects) {
        while (!isCancelled()) {
            this.requestPlayerStatus();
            try {
                Thread.sleep(DEFAULT_TIMEOUT);
            } catch (InterruptedException e) {
                e.printStackTrace();
                this.cancel(false);
            }
        }

        return null;
    }


    /**
     * Send a HTTP request to get the latest player+lobby details
     */
    private void requestPlayerStatus() {
        Call<JSend<PlayerStatus>> call = service.joinAGame(this.session.getToken());
        call.enqueue(this);
    }

    /**
     * Invoked for a received HTTP response.
     * <p>
     * Note: An HTTP response may still indicate an application-level failure such as a 404 or 500.
     * Call {@link Response#isSuccessful()} to determine if the response indicates success.
     *
     * @param call
     * @param response
     */
    @Override
    public void onResponse(@NonNull Call<JSend<PlayerStatus>> call, @NonNull Response<JSend<PlayerStatus>> response) {
        if (response.body() != null) {
            this.listener.updatePlayerStatus(response.body().getData());
        }
    }

    /**
     * Invoked when a network exception occurred talking to the server or when an unexpected
     * exception occurred creating the request or processing the response.
     *
     * @param call
     * @param t
     */
    @Override
    public void onFailure(@NonNull Call<JSend<PlayerStatus>> call, @NonNull Throwable t) {

    }
}
