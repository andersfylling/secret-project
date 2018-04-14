package team.adderall.game.level;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cim on 14/4/18.
 */

import team.adderall.game.framework.GamePainter;
import team.adderall.game.framework.UpdateRateCounter;
import team.adderall.game.framework.component.GameComponent;
import team.adderall.game.framework.component.GameDepWire;

@GameComponent("level")
public class LevelManager
            implements GamePainter {
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

        /**
         * @param width              width of drawable area
         * @param height             height of drawable area
         * @param pointsInWidth      n1-n2 = line, n2-n3: space, n3-n4: line, etc.
         * @param minimumLevelsReady how many levels/floors should always be predefined
         * @param thickness          how thick should each floor/line/level be
         * @param gameSeed           affects floor/level generation
         */
        @GameDepWire
        public LevelManager(int width, int height, int pointsInWidth, int minimumLevelsReady, int thickness, long gameSeed) {
            this.levelsOnScreen = minimumLevelsReady;
            this.blocksInWidth = pointsInWidth;

            this.width = width;
            this.height = height;

            this.thickness = thickness;

            this.generator = new LevelGenerator(gameSeed, Floor.TYPE_HIGHEST_INDEX);
            this.levels = new ArrayList<>();

            // solid floor
            Floor solidFloor = this.generator.generateSolidFloor(width);
            this.levels.add(solidFloor);

            // generate the levels
            for (int i = 1; i < height; i++) {
                final Floor floor = this.generator.generateFloor(i, this.levels.get(i - 1), width);
                this.levels.add(floor);
            }

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

        public List<Floor> getFloors() {
            return this.levels;
        }

        public int getThickness() {
            return this.thickness;
        }

        @Override
        public void paint(Canvas canvas) {
            int counter = 0;
            starValue++;
            for (Floor floor : this.levels) {
                floor.paint(canvas, this.painters, this.height - (counter * this.thickness) + starValue * 5);

                // TODO: ... xD
                counter++; // next level
                counter++; // add some spacing between levels for the ball to roll
                counter++; // add some space for jumping between levels
                counter += 2;
            }
        }

        public int getHeight() {
            return height;
        }
    }