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
    private int highestXValue;
    private Paint painter;


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
        if(newScore > this.highestXValue)
            this.highestXValue = newScore;
    }

    public void paint(Canvas canvas) {
        final String text = "HighScore" + highestXValue;
        canvas.drawText(text, 50, 100, this.painter);
    }


}
