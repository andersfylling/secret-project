package team.adderall.game;

import team.adderall.game.framework.Manager;

public class PaintManager
    implements Manager
{
    @Override
    public void run() {
        System.out.print("paint run");
    }
}
