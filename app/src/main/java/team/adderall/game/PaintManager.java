package team.adderall.game;

import team.adderall.game.framework.GamePainter;

public class PaintManager
    implements GamePainter
{
    @Override
    public void run() {
        System.out.print("paint run");
    }
}
