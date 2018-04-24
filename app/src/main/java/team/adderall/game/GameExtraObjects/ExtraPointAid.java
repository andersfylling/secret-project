package team.adderall.game.GameExtraObjects;

import team.adderall.game.ball.BallManager;


public class ExtraPointAid extends Aid {
    /**
     * Initialise Aid with floor type id, and points to gain for hitting it
     *
     * @param type
     */
    private int points;
    ExtraPointAid(int type, int points, int chance, int color) {
        super(type,chance,color);
        this.points = points * this.REALPOINTVALUE;
    }

    @Override
    public void handleCollision(BallManager player){
        player.updateAidExtraScore(this.points);
    }
}
