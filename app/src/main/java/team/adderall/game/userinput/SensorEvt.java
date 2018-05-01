package team.adderall.game.userinput;

@Deprecated
public class SensorEvt {
    public final static int INDEX_X = 0;
    public final static int INDEX_Y = 1;

    private final float x;
    private final float y;

    public SensorEvt(final float[] data) {
        this.x = data[INDEX_X];
        this.y = data[INDEX_Y];
    }

    public SensorEvt(final float x, final float y) {
        this.x = x;
        this.y = y;
    }

    public float[] getData() {
        return new float[]{this.x, this.y};
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }
}
