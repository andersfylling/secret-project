package team.adderall.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import team.adderall.game.gameloop.GamePainter;

public class UpdateRateCountPainter
        implements GamePainter
{
    private final static int TIMEOUT = 25;

    private final Paint painter;
    private final UpdateRateCounter updateRateCounter;

    private long lastRun;
    private String lastAVGRate;

    private String prefix;
    private int x;
    private int y;

    public UpdateRateCountPainter(final UpdateRateCounter updateRateCounter) {
        this.painter = new Paint();
        this.painter.setTextSize(40);
        this.painter.setColor(Color.RED);

        this.updateRateCounter = updateRateCounter;

        this.prefix = "rate: ";
        this.x = 50;
        this.y = 50;
        lastRun = System.currentTimeMillis();
        lastAVGRate = "0";
    }

    @Override
    public void paint(Canvas canvas) {

    }

    @Override
    public void paint(Canvas canvas, float y) {
        if (lastRun + TIMEOUT < System.currentTimeMillis()) {
            this.lastAVGRate = Long.toString(this.updateRateCounter.getUpdateRate());
            this.lastRun = System.currentTimeMillis();
        }
        final String text = this.prefix + this.lastAVGRate;
        canvas.drawText(text, this.x, y+this.y, this.painter);
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setHexColourCode(final String hexColourCode) {
        this.painter.setColor(Color.parseColor(hexColourCode));
    }

    public void setTextSize(final int size) {
        this.painter.setTextSize(size);
    }
}
