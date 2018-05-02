package team.adderall.game.highscore;

import android.graphics.Canvas;

import team.adderall.game.GameState;
import team.adderall.game.Player;
import team.adderall.game.Players;
import team.adderall.game.framework.GamePainter;
import team.adderall.game.framework.component.GameComponent;
import team.adderall.game.framework.component.GameDepWire;
import team.adderall.game.framework.component.Inject;


@GameComponent
public class DrawHighScore
        implements
        GamePainter
{
    private final Players players;
    private final GameState gameState;

    @GameDepWire
    public DrawHighScore(@Inject("players") Players p,
                         @Inject("GameState") GameState gameState)
    {
        this.players = p;
        this.gameState = gameState;
    }


    @Override
    public void paint(Canvas canvas) {
        int y = (int) gameState.getyScrollValue();
        int playerNumber = 0;
        for(Player player : players.getPlayers()){
            player.getBallManager().drawHighScore(canvas,y, player.getName(),playerNumber);
            playerNumber++;
        }
    }

    @Override
    public void paint(Canvas canvas, float y) {
    }
}
