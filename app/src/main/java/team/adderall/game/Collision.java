package team.adderall.game;

import android.graphics.Point;
import android.graphics.Rect;

import java.util.List;

import team.adderall.game.ball.BallManager;
import team.adderall.game.framework.GameLogicInterface;
import team.adderall.game.framework.component.GameComponent;
import team.adderall.game.framework.component.GameDepWire;
import team.adderall.game.framework.component.GameLogic;
import team.adderall.game.framework.component.Inject;
import team.adderall.game.level.Floor;
import team.adderall.game.level.LevelManager;

@GameComponent
@GameLogic(wave = 3)
public class Collision
        implements GameLogicInterface {
    private final Players players;
    private final LevelManager level;
    private boolean hitOnTop = false;
    @GameDepWire
    public Collision(@Inject("players") Players p,
                     @Inject("level") LevelManager level)
    {
        this.players = p;
        this.level = level;
    }

    /**
     * Given two rect, return the new value to rect x, such that they no longer intercect.
     * This function assumes they do intercect.
     */
    public Point getNoneIntercetCord(Rect x, Rect y){
        int newX = x.centerX();
        int newY = x.centerY();

        if(x.top <= y.top) {
            newY = y.top -x.height()/2;
            this.hitOnTop = true;
        }else if(x.bottom >= y.bottom) {
            newY = y.bottom +x.height()/2;
        }
        else if(x.left < y.left) {
            newX = y.left - x.width()/2;
        }
        else if(x.right > y.right) {
            newX = y.right + x.width()/2;
        }
        return new Point(newX,newY);
    }

    @Override
    public void run() {
        int thickness = level.getThickness();
        int height = level.getHeight();
        this.hitOnTop = false;
        int y = 0;

        for(Player player: players.getAlivePlayers()) {
            BallManager bm = player.getBallManager();
            int x1 = (int) bm.getX();
            int y1 = (int) bm.getY();
            for (Floor floor : level.getFloors()) {
                Rect collide = floor.checkColition(height - (y * thickness), x1, y1, thickness);

                if (collide != null) {
                    Point p = getNoneIntercetCord(getRect(x1, y1), collide);
                    bm.setPos(p.x, p.y);
                    bm.setVelocity(0);

                    if(this.hitOnTop) {
                        bm.setAtGround(true);
                    }
                }
            }
        }

    }

    private Rect getRect(int x, int y)
    {
        return new Rect(x - GameState.FIXED_BALL_RADIUS,
                y - GameState.FIXED_BALL_RADIUS,
                x + GameState.FIXED_BALL_RADIUS,
                y + GameState.FIXED_BALL_RADIUS);
    }
}
