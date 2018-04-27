package team.adderall;

public class Client
{
    private String address;

    public Client(final String address) {
        this.address = address; // url + port + prefix
    }

    public boolean ping() {
        throw new NotImplementedException();
    }
    public void pingThread(final ClientCallback cb) {
        (new Thread(() -> {
            boolean reachable = ping();
            cb.trigger(reachable);
        })).start();
    }


    public UserSession authenticate() {
        throw new NotImplementedException();
    }

    public void authenticateThread(final ClientCallback cb) {
        (new Thread(() -> {
            UserSession session = authenticate();
            if (session != null) {
                cb.trigger(session);
            }
        })).start();
    }

}
