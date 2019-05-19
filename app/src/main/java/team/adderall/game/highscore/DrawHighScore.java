package team.adderall.game.highscore;

import android.graphics.Canvas;

import addy.annotations.*;
import team.adderall.game.GameState;
import team.adderall.game.player.Player;
import team.adderall.game.player.Players;
import team.adderall.game.gameloop.GamePainter;

@Service
public class DrawHighScore
        implements
        GamePainter
{
    private final Players players;
    private final GameState gameState;

    @DepWire
    public DrawHighScore(@Inject("players") Players p,
                         @Inject("GameState") GameState gameState)
    {
        this.players = p;
        this.gameState = gameState;
    }


    @Override
    public void paint(Canvas canvas) {
        int y = (int) gameState.getYScrollValue();
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
