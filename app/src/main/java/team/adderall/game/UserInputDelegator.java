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
    private Jumping jumping;
    private DeltaTime deltaTime;
    private Player player;

    @GameDepWire
    public UserInputDelegator(@Inject("userInputHolder") UserInputHolder h,
                              @Inject("jumping") Jumping j,
                              @Inject("deltaTime") DeltaTime dt,
                              @Inject("players") Players p)
    {
        holder = h;
        player = p.getActive();
        deltaTime = dt; // game speed
        jumping = j;
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
        jump();
        movePlayer();
    }

    private void jump() {
        boolean jump = holder.jumping();

        if (jump) {
            jumping.run();
        }
    }

    private void movePlayer() {
        double movement = holder.xAxisMovement();
        movement *= deltaTime.getSpeed();
        player.getBallManager().addToX(movement);
    }
}
