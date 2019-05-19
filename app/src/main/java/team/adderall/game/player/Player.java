package team.adderall.game.player;

import team.adderall.game.ball.BallManager;
import team.adderall.game.physics.Collidable;
import team.adderall.game.physics.GravityAffected;
import team.adderall.game.physics.Velocity;
import team.adderall.game.physics.XPosition;
import team.adderall.game.physics.XYPosition;
import team.adderall.game.physics.YPosition;

/**
 * Details about a player.
 * We've implemented different physics related interfaces
 * since the Player instance is what we generally use during calculations.
 * TODO-0: inject player instances into a entity class such that Gravity, Collision, etc.
 * TODO-1: doesn't need to be aware of players directly.
 */
public class Player
        implements
        YPosition,
        XPosition,
        XYPosition,
        Velocity,
        GravityAffected,
        Collidable
{
    private String id;
    private String name;
    private int score;

    // ensure he is in the correct game + position
    private long gameID;
    private long userID;

    private boolean activePlayer;

    private BallManager ballManager;
    private Long gameToken;


    public Player() {
        init();
    }
    public Player(boolean activePlayer) {
        init();
        this.activePlayer = activePlayer;
    }

    private void init() {
        id = "";
        name = "";
        score = -1;
        gameID = 0;
        userID = 0;
        activePlayer = false;
        ballManager = null;
        gameToken = 0L;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return ballManager.getScore();
        //return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public long getGameID() {
        return gameID;
    }

    public void setGameID(long gameID) {
        this.gameID = gameID;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public boolean isActivePlayer() {
        return activePlayer;
    }

    public void setActivePlayer(boolean activePlayer) {
        this.activePlayer = activePlayer;
    }

    public void createBallManager() {
        if (this.ballManager != null) {
            return;
        }

        this.ballManager = new BallManager(activePlayer);
    }

    public BallManager getBallManager() {
        return ballManager;
    }

    public Long getGameToken() {
        return gameToken;
    }

    public void setGameToken(Long gameToken) {
        this.gameToken = gameToken;
    }



    // The code below is outdated, as we haven't removed BallManager yet.
    // TODO: BallManager is legacy code and should be removed

    @Override
    public double y() {
        return ballManager.getY(); // potential null pointer exception
    }

    @Override
    public void y(double y) {
        ballManager.setY(y); // potential null pointer exception
    }

    @Override
    public double velocity() {
        return ballManager.getVelocity(); // potential null pointer exception
    }

    @Override
    public void velocity(double velocity) {
        ballManager.setVelocity(velocity); // potential null pointer exception
    }

    @Override
    public void setPos(double x, double y) {
        ballManager.setPos(x, y); // potential null pointer exception
    }

    @Override
    public double x() {
        return ballManager.getX(); // potential null pointer exception
    }

    @Override
    public void x(double x) {
        ballManager.setPos(x, ballManager.getY());  // potential null pointer exception
    }

    @Override
    public void setAtGround(boolean atGround) {
        ballManager.setAtGround(atGround);
    }

    @Override
    public boolean getAtGround() {
        return ballManager.getAtGround();
    }
}
