package team.adderall.game.gameloop;

import android.graphics.Canvas;

public interface GamePainter
{
    void paint(final Canvas canvas);
    void paint(final Canvas canvas,float y);

}
