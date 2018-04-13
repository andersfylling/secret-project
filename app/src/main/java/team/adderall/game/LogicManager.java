package team.adderall.game;

import team.adderall.game.framework.GameLogicInterface;

public class LogicManager
        implements GameLogicInterface
{
    public LogicManager() {
    }

    @Override
    public void run() {
        System.out.println("logic run");
    }
}
