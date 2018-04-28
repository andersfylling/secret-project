package team.adderall.game.highscore;

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
public class HighScore {
    /**
     * Declare different values the person would be scored on
     */
    private double highestXValue = 0; // highest x value?
    private int aidHighScore = 0;
    private Paint painter = null;


    public void HighScore(){
        // TODO: Does it not call this?
        this.reset();

    }

    public double getHighestXValue(){
        return this.highestXValue;
    }
    public void potensiallySetHighestXValue(double newScore){
        if(newScore < this.highestXValue)
            this.highestXValue = newScore;
    }

    public int getScaledHighScore(){
        int score = (int) this.highestXValue + this.aidHighScore;
        return score/-100;
    }

    /**
     * Should these things be painted on a seperat view? on top of the other view.
     * So that we wont need to update the y position?
     * @param canvas
     */
    public void paint(Canvas canvas,float y) {
        final String text = "HighScore: " + Integer.toString(getScaledHighScore());
        if(this.painter == null){
            this.reset();

        }
        canvas.drawText(text, 50, y+200, this.painter);
    }

    private void reset() {
        this.painter = new Paint();
        this.painter.setTextSize(40);
        this.painter.setColor(Color.RED);
        this.highestXValue = 0;
        this.aidHighScore = 0;
    }

    /**
     * Update the Extra score recieved by the aid.
     * @param score
     */
    public void updateAidExtraScore(int score) {
        this.aidHighScore +=score;
    }
}
