package team.adderall.game;

public class UpdateRateCount
        implements UpdateRateCounter
{
    private long lastUpdate;
    private long avgRate;

    public UpdateRateCount() {
        this.lastUpdate = System.nanoTime();
        this.avgRate = 0;
    }

    @Override
    public void update() {
        long now = System.nanoTime();
        this.avgRate = 1000000000 / (now - this.lastUpdate);
        this.lastUpdate = now;
    }

    @Override
    public long getUpdateRate() {
        return this.avgRate;
    }
}
