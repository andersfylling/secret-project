package team.adderall.game;

import team.adderall.game.framework.Logicer;

public class LogicManager
        implements Logicer
{
    public LogicManager() {
    }

    @Override
    public void run() {
        System.out.print("logic run");
    }
}
