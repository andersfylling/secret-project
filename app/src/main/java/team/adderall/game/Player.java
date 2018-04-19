package team.adderall.game;

/**
 * Details about a player
 */
public class Player {
    private String id;
    private String name;
    private int score;

    // ensure he is in the correct game + position
    private long gameID;
    private long userID;

    private boolean activePlayer;


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
        return score;
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
}
