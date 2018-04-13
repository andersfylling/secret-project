package team.adderall.game;

import team.adderall.game.framework.Manager;
import team.adderall.game.framework.Painter;

public class PaintManager
    implements Painter
{
    @Override
    public void run() {
        System.out.print("paint run");
    }
}
