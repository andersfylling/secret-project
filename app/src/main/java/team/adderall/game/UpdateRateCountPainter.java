package team.adderall.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import team.adderall.game.framework.GamePainter;
import team.adderall.game.framework.UpdateRateCounter;
import team.adderall.game.framework.component.GameComponent;
import team.adderall.game.framework.component.GameDepWire;

public class UpdateRateCountPainter
        implements GamePainter
{
    private final Paint painter;
    private final UpdateRateCounter updateRateCounter;

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
    }

    @Override
    public void paint(Canvas canvas) {
        final String text = this.prefix + Long.toString(this.updateRateCounter.getUpdateRate());
        canvas.drawText(text, this.x, this.y, this.painter);
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
