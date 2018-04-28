package team.adderall.game.level;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cim on 14/4/18.
 */

import team.adderall.game.GameDetails;
import team.adderall.game.GameExtraObjects.Aid;
import team.adderall.game.framework.GamePainter;
import team.adderall.game.framework.UpdateRateCounter;
import team.adderall.game.framework.component.GameComponent;
import team.adderall.game.framework.component.GameDepWire;
import team.adderall.game.framework.component.Inject;

@GameComponent("level")
public class LevelManager
        implements
        GamePainter
{
    private final static int FIXED_WIDTH = 1500;
    private List<Floor> levels;

    private LevelGenerator generator;

    private int width;
    private int height;

    private int blocksInWidth;
    private int levelsOnScreen; // how many levels to always have ready
    private int verticalBlockSize;
    private int horizontalBlockSize;
    private int thickness;

    private Paint[] painters;
    private Paint blockPainter;
    private Paint spacePainter;
    int starValue = 0;
    private ArrayList<Aid> aids;

    /**
     * @param width              width of drawable area
     * @param height             height of drawable area
     * @param pointsInWidth      n1-n2 = line, n2-n3: space, n3-n4: line, etc.
     * @param minimumLevelsReady how many levels/floors should always be predefined
     * @param thickness          how thick should each floor/line/level be
     * @param gameSeed           affects floor/level generation
     */
    @GameDepWire
    public LevelManager(@Inject("canvasSize") Point canvasSize,
                        //int pointsInWidth,
                        //int minimumLevelsReady,
                        //int thickness,
                        //long gameSeed
                        @Inject("GameDetails") GameDetails gameDetails)
    {
        //this.levelsOnScreen = minimumLevelsReady;
        //this.blocksInWidth = pointsInWidth;

        this.width = canvasSize.x;
        this.height = canvasSize.y;

        this.thickness = 100;//thickness;

        this.generator = new LevelGenerator(gameDetails.getGameSeed(), Floor.TYPE_HIGHEST_INDEX);
        this.levels = new ArrayList<>();

        // solid floor
        Floor solidFloor = this.generator.generateSolidFloor(width);
        this.levels.add(solidFloor);

        // setup painters
        this.painters = new Paint[Floor.TYPE_HIGHEST_INDEX + 1];

        this.painters[Floor.TYPE_AIR] = new Paint();
        this.painters[Floor.TYPE_AIR].setStyle(Paint.Style.FILL);
        this.painters[Floor.TYPE_AIR].setColor(Color.RED);

        this.painters[Floor.TYPE_SOLID] = new Paint();
        this.painters[Floor.TYPE_SOLID].setStyle(Paint.Style.FILL);
        this.painters[Floor.TYPE_SOLID].setColor(Color.BLUE);
        this.painters[Floor.TYPE_SOLID].setStrokeWidth(thickness);
    }

    public List<Floor> getFloors()
    {
        return this.levels;
    }

    public int getThickness()
    {
            return this.thickness;
        }


    /**
     * Get the Aids list from the AidsHandler.
     * Also generete the floors now that we have the correct Aid Types
     * After that we would also initialise all the correct painters.
     * @param aids
     */
    public void setAids(ArrayList<Aid> aids)
    {
        this.aids = aids;
        this.generator.setAids(aids);

        // generate the levels
        for (int i = 1; i < this.height; i++) {
            final Floor floor = this.generator.generateFloor(i, this.levels.get(i - 1), FIXED_WIDTH);
            this.levels.add(floor);
        }

        // Initialise all the correct painters for aids
        for (Aid aid : aids) {
            int type = aid.getType();
            this.painters[type] = aid.getPainter();
        }

    }

    @Override
    public void paint(Canvas canvas)
    {
        int counter = 0;
        //starValue++;
        for (Floor floor : this.levels) {
            floor.paint(canvas, this.painters, this.height - (counter * this.thickness) + (int)(starValue * 0));

            // TODO: ... xD
            counter++; // next level
            counter++; // add some spacing between levels for the ball to roll
            counter++; // add some space for jumping between levels
            counter += 2;
        }
    }

    @Override
    public void paint(Canvas canvas, float y)
    {}

    public int getHeight()
    {
        return height;
    }
}