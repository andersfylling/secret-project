package team.adderall.game.GameExtraObjects;

import android.graphics.Paint;

import team.adderall.game.ball.BallManager;


public class Buff {
    private final int type;
    private final int chance;
    private final Paint painter;
    public int REALPOINTVALUE = -100;

    /**
     * Initialise Buff with floor type id.
     * @param type
     * @param color
     */
    Buff(int type, int chance, int color){
        this.type = type;
        this.chance = chance;

        this.painter = new Paint();
        this.painter.setStyle(Paint.Style.FILL);
        this.painter.setColor(color);
    }

    /**
     * When coliding with a object of this type
     * This would trigger the function call that would do the reward.
     * @param player
     */
    public void handleCollision(BallManager player){
        player.updateAidExtraScore(-100);
    }

    public int getType() {
        return type;
    }

    public int getChance() {
        return this.chance;
    }

    public Paint getPainter(){
        return this.painter;
    }

}
