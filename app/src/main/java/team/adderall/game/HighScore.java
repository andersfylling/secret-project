package team.adderall.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import team.adderall.game.ball.BallManager;
import team.adderall.game.framework.GameLogicInterface;
import team.adderall.game.framework.GamePainter;
import team.adderall.game.framework.component.GameComponent;
import team.adderall.game.framework.component.GameDepWire;
import team.adderall.game.framework.component.Inject;

/**
 * Created by Cim on 17/4/18.
 */
class HighScore {
    /**
     * Declare different values the person would be scored on
     */
    private int highestXValue = 0;
    private Paint painter = null;


    public void HighScore(){
        this.highestXValue = 0;

        this.painter = new Paint();
        this.painter.setTextSize(40);
        this.painter.setColor(Color.RED);

    }

    public int getHighestXValue(){
        return this.highestXValue;
    }
    public void potensiallySetHighestXValue(int newScore){
        if(newScore < this.highestXValue) {
            this.highestXValue = newScore;
        }
    }

    /**
     * Should these things be painted on a seperat view? on top of the other view.
     * So that we wont need to update the y position?
     * @param canvas
     */
    public void paint(Canvas canvas,float y) {
        int scaledHighScore = highestXValue/-100;
        final String text = "HighScore: " + Integer.toString(scaledHighScore);
        if(this.painter == null){
            this.painter = new Paint();
            this.painter.setTextSize(40);
            this.painter.setColor(Color.RED);
        }
        canvas.drawText(text, 50, y+200, this.painter);
    }


}
