package team.adderall.game.level;

/**
 * Created by Cim on 14/4/18.
 */


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LevelGenerator
{
    private final long seed;
    private final int maxNum;

    public LevelGenerator(final long seed, final int maxNum) {
        this.seed = seed;
        this.maxNum = maxNum + 1;
    }

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
            Line l = new Line(types[counter++], previousX, nextX > width ? width : nextX);
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

    public Floor generateSolidFloor(final int width) {
        List<Line> lines = new ArrayList<>();
        lines.add(new Line(Floor.TYPE_SOLID, 0, width));

        return new Floor(lines);
    }
}

