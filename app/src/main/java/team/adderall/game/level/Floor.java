package team.adderall.game.level;

/**
 * Created by Cim on 14/4/18.
 */


import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;

public class Floor
{
    public final static int TYPE_AIR = 0;
    public final static int TYPE_SOLID = 20;

    public final static int TYPE_LOWEST_INDEX = TYPE_AIR;
    public final static int TYPE_HIGHEST_INDEX = TYPE_SOLID;

    private List<Line> lines;
    int y = 0;

    public Floor(final List<Line> lines) {
        this.lines = lines;
    }

    public List<Line> getLines() {
        return lines;
    }

    public void paint(Canvas canvas, Paint[] painters, int y) {
        this.y = y;
        for (Line line : this.lines) {
            final Paint painter = painters[line.getFloorType()];
            if (painter == null) {
                continue;
            }

            canvas.drawLine(line.getX1(), y, line.getX2(), y, painter);
        }
    }

    public Rect checkColition(int y, ArrayList<Integer> pos, int thickness){
        for (Line line : this.lines) {
            if(line.getFloorType() == 0) continue;
            Rect squere = new Rect(line.getX1(), this.y, line.getX2(), this.y +thickness);
            Rect ball = new Rect(pos.get(0) -45, pos.get(1)-45, pos.get(0) + 45, pos.get(1)+45);

            if(ball.intersect(squere)){
                return squere;
            }
        }
        return null;
    }
}
