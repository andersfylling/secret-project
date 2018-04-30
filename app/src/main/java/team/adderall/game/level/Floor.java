package team.adderall.game.level;

/**
 * Created by Cim on 14/4/18.
 */


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

import java.util.List;

import team.adderall.game.GameState;

public class Floor
{
    public final static int TYPE_AIR = 0;
    public final static int TYPE_COIN = 1;
    public static final int TYPE_SUPERCOIN = 2;
    public final static int TYPE_SOLID = 10;

    public final static int TYPE_LOWEST_INDEX = TYPE_AIR;
    public final static int TYPE_HIGHEST_INDEX = TYPE_SOLID;


    private List<Line> lines;
    int y = 0;
    Paint p;

    public Floor(final List<Line> lines) {
        this.lines = lines;
        this.p = new Paint();
        p.setColor(Color.RED);

    }

    public List<Line> getLines() {
        return lines;
    }

    public void paint(Canvas canvas, Paint[] painters, int y, float scale) {
        this.y = y;
        for (Line line : this.lines) {

            // Scale the cordinates to fit the screen size
            int x1 = (int) (line.getX1()*scale);
            int x2 = (int) (line.getX2()*scale);

            int floorType = line.getFloorType();
            floorType = floorType/TYPE_SOLID;
            floorType *=10;

            final Paint painter = painters[floorType];
            if (painter == null) {
                continue;
            }
            canvas.drawLine(x1, y, x2, y, painter);

            Rect aid = getAid(line.getFloorType()%TYPE_SOLID,x1,y,x2);

            if(aid != null) {
                final Paint Aidpainter = painters[line.getFloorType()%TYPE_SOLID];
                if (Aidpainter == null) {
                    continue;
                }

                canvas.drawRect(aid,Aidpainter);

            }
        }
    }

    private Rect getAid(int floorType,int x, int y, int x1) {
        if(floorType!= 0)
        {
            int mid = (x1 +x)/2;
            return new Rect(mid-GameState.FIXED_AID_LENGTH,
                    y-GameState.FIXED_AID_HEIGHT,
                    mid+GameState.FIXED_AID_LENGTH,
                    y);
        }
        return null;
    }

    public int aidColision(int x, int y){
        for (Line line : this.lines) {
            Rect aid = getAid(line.getFloorType()%TYPE_SOLID,line.getX1(),this.y,line.getX2());
            if (aid == null) continue;
            Rect ball = new Rect(x - GameState.FIXED_BALL_RADIUS,
                    y-GameState.FIXED_BALL_RADIUS,
                    x + GameState.FIXED_BALL_RADIUS,
                    y+GameState.FIXED_BALL_RADIUS);
            if(ball.intersect(aid)){
                /**
                 * Return aid type,
                 * And change florType
                 * Then the aidCollisionHandler would perform the aid importance
                 */
                int floortype = line.getFloorType()/TYPE_SOLID;
                int realtype = line.getFloorType() %TYPE_SOLID;
                floortype = floortype * TYPE_SOLID;
                line.setFloorType(floortype);
                return realtype;
            }
        }
        return -1;
    }

    public Rect checkColition(int y, int x1, int y1, int thickness){
        for (Line line : this.lines) {
            if(line.getFloorType() < TYPE_SOLID) continue;
            Rect squere = new Rect(line.getX1(), this.y, line.getX2(), this.y +thickness);

            Rect ball = new Rect(x1 -GameState.FIXED_BALL_RADIUS,
                    y1-GameState.FIXED_BALL_RADIUS,
                    x1 + GameState.FIXED_BALL_RADIUS,
                    y1+GameState.FIXED_BALL_RADIUS);

            if(ball.intersect(squere)){
                return squere;
            }
        }
        return null;
    }
}
