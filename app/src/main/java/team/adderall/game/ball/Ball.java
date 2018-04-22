package team.adderall.game.ball;


public class Ball
{
    private final static String DEFAULT_COLOUR = "#CD5C5C";
    private final static int DEFAULT_RADIUS = 60;

    private String colour;
    private int radius;

    public Ball() {
        this.radius = DEFAULT_RADIUS;
        this.colour = DEFAULT_COLOUR;
    }

    public Ball(final int radius) {
        this.radius = radius;
        this.colour = DEFAULT_COLOUR;
    }

    public Ball(final int radius, final String colour) {
        this.radius = radius;
        this.colour = colour;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }
}
