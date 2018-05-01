package team.adderall.game.level;


import com.google.android.gms.games.Game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import team.adderall.game.GameExtraObjects.AidsHandler;
import team.adderall.game.GameExtraObjects.Aid;
import team.adderall.game.GameState;

/**
 * This class handles level generation
 */
public class LevelGenerator
{
    private final long seed;
    private final int maxNum;
    private ArrayList<Aid> aids;

    /**
     * Used to make sure that a ball can pass each level
     */
    private static int EXTRA_GAP = GameState.FIXED_BALL_RADIUS/5;
    private static int MINIMUM_GAP = GameState.FIXED_BALL_RADIUS * 2;
    private static int GAP = MINIMUM_GAP + EXTRA_GAP;

    public LevelGenerator(final long seed, final int maxNum) {
        this.seed = seed;
        this.maxNum = maxNum + 1;
        this.aids = null;
    }

    /**
     * Generate floor
     * @param floor
     * @param previousFloor
     * @param width
     * @return floor
     */
    public Floor generateFloor(final int floor, final Floor previousFloor, final int width) {



        if (previousFloor == null) {
            throw new NullPointerException("list is null");
        }

        final List<Line> lines = new ArrayList<>();

        long sum = 0;
        int blockIndex = 0;
        for (final Line line : previousFloor.getLines()) {
            sum += line.getX1() + line.getX2()*blockIndex;
            blockIndex++;
        }

        // Random, when given a seed, always print the same sequence of numbers.
        // to avoid creating the same level for each floor, we manipulate the
        // the seed product based on values from the previous level and the current floor.
        Random r = new Random(this.seed + (floor * previousFloor.getLines().size()) + sum);
        Random r2 = new Random(this.seed + (floor * previousFloor.getLines().size()) + sum);

        // TODO: properly randomize,and set a minimum gap. or at least ensure one valid opening.
        int[] types = new int[]{
                Floor.TYPE_SOLID,
                Floor.TYPE_AIR,
                Floor.TYPE_SOLID
        };
        int previousX = 0;
        int counter = 0;
        while (previousX < width) {
            int nextX = previousX + r.nextInt(width/3);

            /**
             * Make sure that airObjects has width bigger than fixed_Ball_radius
             */
            if(types[counter] == Floor.TYPE_AIR)
            {
                if (nextX - previousX < GAP)
                {
                    nextX = previousX + GAP;
                }
            }

            Line l = new Line(types[counter++], previousX, nextX > width ? width : nextX);
            l = potensiallyChangeline(l,r2);
            lines.add(l);

            previousX = nextX;
            if (counter == 3) {
                counter = 0;
            }
        }

        // TODO: ensure the new level can be reached from the previous one.
        // worries: might be blocking the player from jumping up.


        return new Floor(lines);
    }

    /**
     * Set the line to potensially be a Aid object also
     * @param l
     * @param r2
     * @return
     */
    private Line potensiallyChangeline(Line l, Random r2) {
        if(aids == null) return l;

        int curPotensialObject = r2.nextInt(aids.size());
        int curChance = aids.get(curPotensialObject).getChance();

        if(r2.nextInt(curChance) == 0){
            l.setFloorType(l.getFloorType()+curPotensialObject+1);
        }
        return l;
    }

    /**
     * Generate Solid floor
     * @param width
     * @return floor
     */
    public Floor generateSolidFloor(final int width) {
        List<Line> lines = new ArrayList<>();
        lines.add(new Line(Floor.TYPE_SOLID, 0, width));

        return new Floor(lines);
    }

    /**
     * Set Aids
     * @param aids
     */
    public void setAids(ArrayList<Aid> aids) {
        this.aids = aids;
    }
}

