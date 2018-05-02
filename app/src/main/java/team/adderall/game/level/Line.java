package team.adderall.game.level;

public class Line {
    private int floorType;
    private final int x1;
    private final int x2;

    public Line(final int floorType, final int x1, final int x2) {
        this.floorType = floorType;
        this.x1 = x1;
        this.x2 = x2;
    }

    public int getFloorType() {
        return floorType;
    }

    public int getX1() {
        return x1;
    }

    public int getX2() {
        return x2;
    }
    public void setFloorType(int floorType) {
        this.floorType = floorType;
    }
}


