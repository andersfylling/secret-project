package team.adderall.game.level;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

import java.util.List;

import team.adderall.game.GameState;

public class Floor
{
    /**
     * Floor types
     */
    public final static int TYPE_AIR = 0;
    public final static int TYPE_SOLID = 10;

    public final static int TYPE_LOWEST_INDEX = TYPE_AIR;
    public final static int TYPE_HIGHEST_INDEX = TYPE_SOLID;

    /**
     * Buff types
     */
    public final static int TYPE_COIN = 1;
    public static final int TYPE_SUPERCOIN = 2;


    private List<Line> lines;
    int y = 0;
    Paint p;

    /**
     * Floor
     * @param lines
     */
    public Floor(final List<Line> lines) {
        this.lines = lines;
        this.p = new Paint();
        p.setColor(Color.RED);

    }

    /**
     * Get lines
     * @return lines
     */
    public List<Line> getLines() {
        return lines;
    }

    /**
     * Get the basetype for a given floortype
     * Written generically in case we add more basetypes
     * @param floortype
     */
    private int getBaseType(int floortype){
        floortype = (int)Math.floor((double)floortype/(double)TYPE_SOLID);
        floortype *=TYPE_SOLID;

        return floortype;
    }

    /**
     * Paint
     * @param canvas
     * @param painters
     * @param y
     * @param scale
     */
    public void paint(Canvas canvas, Paint[] painters, int y, float scale) {
        this.y = y;
        for (Line line : this.lines) {

            // Scale the cordinates to fit the screen size
            int x1 = (int) (line.getX1()*scale);
            int x2 = (int) (line.getX2()*scale);

            int floorType = getBaseType(line.getFloorType());

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

    /**
     * Get a given Buff
     * @param floorType
     * @param x
     * @param y
     * @param x1
     * @return Rect aid, or NULL
     */
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

    /**
     * Check collision between Ball and Aids
     * @param x
     * @param y
     * @return
     */
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
                int floortype = getBaseType(line.getFloorType());
                int realtype = line.getFloorType() %TYPE_SOLID;
                line.setFloorType(floortype);
                return realtype;
            }
        }
        return -1;
    }

    /**
     * Check colission between ball and lines
     * @param y
     * @param x1
     * @param y1
     * @param thickness
     * @return
     */
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
