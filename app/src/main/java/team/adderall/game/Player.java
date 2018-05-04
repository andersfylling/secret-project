package team.adderall.game;

import team.adderall.game.ball.BallManager;

/**
 * Details about a player
 */
public class Player
    implements
        GravityAffected
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
}
