package team.adderall.game.GameExtraObjects;

import team.adderall.game.ball.BallManager;


interface AidAndNeg {
    void handleCollision(BallManager player);
    int getType();
}
