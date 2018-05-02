package team.adderall.game;

import team.adderall.game.framework.GameLogicInterface;
import team.adderall.game.framework.component.GameComponent;
import team.adderall.game.framework.component.GameDepWire;
import team.adderall.game.framework.component.GameLogic;
import team.adderall.game.framework.component.Inject;
import team.adderall.game.userinput.Jumping;

@GameComponent
@GameLogic(wave=1)
public class UserInputDelegator
    implements GameLogicInterface
{
    private UserInputHolder holder;
    private DeltaTime deltaTime;
    private Players players;

    @GameDepWire
    public UserInputDelegator(@Inject("userInputHolder") UserInputHolder h,
                              @Inject("deltaTime") DeltaTime dt,
                              @Inject("players") Players p)
    {
        holder = h;
        players = p;
        deltaTime = dt; // game speed
    }


    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        for (Player player : players.getAlivePlayers()) {
            jump(player);
            movePlayer(player);
        }
    }

    private void jump(Player player) {
        boolean jump = holder.jumping((int) player.getUserID());

        if (jump) {
            Jumping.jump(player);
        }
    }

    private void movePlayer(Player player) {
        double movement = holder.xAxisMovement((int) player.getUserID());
        movement *= deltaTime.getSpeed();
        player.getBallManager().addToX(movement);
    }
}
