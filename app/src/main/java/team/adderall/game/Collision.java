package team.adderall.game;

import android.graphics.Point;
import android.graphics.Rect;

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
        implements
        GameLogicInterface
{
    private final Players players;
    private final LevelManager level;
    private boolean hitOnTop;

    @GameDepWire
    public Collision(@Inject("players") Players p,
                     @Inject("level") LevelManager l)
    {
        players = p;
        level = l;
        hitOnTop = false;
    }

    /**
     * Given two rect, return the new value to rect x, such that they no longer intercect.
     * This function assumes they do intercect.
     */
    public Point getNoneIntercetCord(Rect x, Rect y)
    {
        int newX = x.centerX();
        int newY = x.centerY();

        if(x.top <= y.top) {
            newY = y.top - x.height()/2;
            hitOnTop = true;
        }
        else if(x.bottom >= y.bottom) {
            newY = y.bottom + x.height()/2;
        }
        else if(x.left < y.left) {
            newX = y.left - x.width()/2;
        }
        else if(x.right > y.right) {
            newX = y.right + x.width()/2;
        }

        return new Point(newX, newY);
    }


    /**
     * Calculate a rectangle for the figure (currently a ball).
     * TODO: create a Figure class, and extract height + width for collision detection.
     *
     * @param x
     * @param y
     * @return
     */
    private Rect getRect(int x, int y)
    {
        return new Rect(x - GameState.FIXED_BALL_RADIUS,
                y - GameState.FIXED_BALL_RADIUS,
                x + GameState.FIXED_BALL_RADIUS,
                y + GameState.FIXED_BALL_RADIUS);
    }

    /**
     * Check if the entity exists within any solid objects from the World (levels)
     * and move the entity outside. We assume the opposite direction
     * (source -> destination, up, left, right, down) can be found by moving the entity
     * to which ever surface is closest.
     *
     * @param entity
     * @param height
     * @param thickness
     */
    public void handleCollisions(Collidable entity, int height, int thickness)
    {
        int x1 = (int) entity.x();
        int y1 = (int) entity.y();

        for (Floor floor : level.getFloors()) {
            Rect collide = floor.checkColition(height/* - (y * thickness)*/, x1, y1, thickness);

            if (collide == null) {
                continue;
            }
            // getNoneIntercetCord sets the "hitOnTop" variable to true
            // if a roof collision is detected
            hitOnTop = false;
            Point p = getNoneIntercetCord(getRect(x1, y1), collide);
            entity.setPos(p.x, p.y);
            entity.velocity(0);

            if(this.hitOnTop) {
                entity.setAtGround(true);
            }

            break; // TODO: review when floors moves on their own, we must continue to check for
                   //       collisions. as of now we assume that once a collision is done, no
                   //       more on a 2D scale can take place since every solid object is
                   //       surrounded by air/void.
        }
    }


    /**
     * Triggered by game loop
     */
    @Override
    public void run()
    {
        int thickness = level.getThickness();
        int height = level.getHeight();

        // players
        for(Collidable entity : players.getAlivePlayers()) {
            handleCollisions(entity, height, thickness);
        }
    }

    public boolean getHitOnTop() {
        return hitOnTop;
    }
}
