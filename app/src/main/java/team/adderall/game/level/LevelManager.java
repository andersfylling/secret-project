package team.adderall.game.level;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;

import team.adderall.game.GameDetails;
import team.adderall.game.GameExtraObjects.Buff;
import team.adderall.game.GameState;
import team.adderall.game.framework.GamePainter;
import team.adderall.game.framework.component.GameComponent;
import team.adderall.game.framework.component.GameDepWire;
import team.adderall.game.framework.component.Inject;

@GameComponent("level")
public class LevelManager
        implements
        GamePainter
{
    private final GameState gameState;
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
    private ArrayList<Buff> buffs;

    /**
     * Level manager constructor
     * @param canvasSize
     * @param gameDetails
     * @param gameState
     */
    @GameDepWire
    public LevelManager(@Inject("canvasSize") Point canvasSize,
                        @Inject("GameDetails") GameDetails gameDetails,
                        @Inject("GameState") GameState gameState)
    {
        this.gameState = gameState;
        this.width = canvasSize.x;
        this.height = canvasSize.y;

        this.thickness = GameState.FIXED_THICKNESS;

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
     * Get the Aids list from the BuffsHandler.
     * Also generete the floors now that we have the correct Buff Types
     * After that we would also initialise all the correct painters.
     * @param buffs
     */
    public void setBuffs(ArrayList<Buff> buffs)
    {
        this.buffs = buffs;
        this.generator.setBuffs(buffs);

        this.gameState.setXScale((float)this.width/(float)GameState.FIXED_WIDTH);
        this.gameState.setYScale((float)this.height/(float)GameState.FIXED_HEIGHT);

        this.painters[Floor.TYPE_AIR] = new Paint();
        this.painters[Floor.TYPE_AIR].setStyle(Paint.Style.FILL);
        this.painters[Floor.TYPE_AIR].setColor(Color.RED);

        this.painters[Floor.TYPE_SOLID] = new Paint();
        this.painters[Floor.TYPE_SOLID].setStyle(Paint.Style.FILL);
        this.painters[Floor.TYPE_SOLID].setColor(Color.BLUE);
        this.painters[Floor.TYPE_SOLID].setStrokeWidth(thickness*gameState.getYScale());


        // generate the levels
        for (int i = 1; i < this.height; i++) {
            final Floor floor = this.generator.generateFloor(i, this.levels.get(i - 1), GameState.FIXED_WIDTH);
            this.levels.add(floor);
        }

        // Initialise all the correct painters for buffs
        for (Buff buff : buffs) {
            int type = buff.getType();
            this.painters[type] = buff.getPainter();
        }

    }

    @Override
    public void paint(Canvas canvas)
    {
        int counter = 0;
        //starValue++;
        for (Floor floor : this.levels) {
            floor.paint(canvas, this.painters, this.height - (counter * this.thickness),gameState.getXScale());

            // TODO: ... xD
            counter++; // next level
            counter++; // add some spacing between levels for the ball to roll
            counter += 2; // add some space for jumping between levels
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