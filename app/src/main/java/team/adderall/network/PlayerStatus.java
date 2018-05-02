package team.adderall.network;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PlayerStatus
        implements
        Serializable,
        Parcelable
{

    @SerializedName("situation")
    @Expose
    private Integer situation;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("players")
    @Expose
    private List<PlayerDetails> players = new ArrayList<>();
    @SerializedName("closed_lobby")
    @Expose
    private Boolean closedLobby;
    @SerializedName("game_id")
    @Expose
    private Integer gameId;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("game_server_addr")
    @Expose
    private String gameServerAddr;
    @SerializedName("game_server_port")
    @Expose
    private Integer gameServerPort;
    @SerializedName("game_seed")
    @Expose
    private Long gameSeed;

    public final static Parcelable.Creator<PlayerStatus> CREATOR = new Creator<PlayerStatus>() {
        @SuppressWarnings({
                "unchecked"
        })
        public PlayerStatus createFromParcel(Parcel in) {
            return new PlayerStatus(in);
        }

        public PlayerStatus[] newArray(int size) {
            return (new PlayerStatus[size]);
        }
    };

    private final static long serialVersionUID = 2882955524697357035L;

    protected PlayerStatus(Parcel in) {
        this.situation = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.players, (PlayerDetails.class.getClassLoader()));
        this.closedLobby = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.gameId = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.userId = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.gameServerAddr = ((String) in.readValue((String.class.getClassLoader())));
        this.gameServerPort = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.gameSeed = ((Long) in.readValue((Long.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     *
     */
    public PlayerStatus() {
    }

    /**
     *
     * @param message
     * @param closedLobby
     * @param userId
     * @param gameId
     * @param players
     * @param situation
     */
    public PlayerStatus(Integer situation, String message, List<PlayerDetails> players, Boolean closedLobby, Integer gameId, Integer userId, String addr, Integer port, Long gameSeed) {
        super();
        this.situation = situation;
        this.message = message;
        this.players = players;
        this.closedLobby = closedLobby;
        this.gameId = gameId;
        this.userId = userId;
        this.gameServerAddr = addr;
        this.gameServerPort = port;
        this.gameSeed = gameSeed;
    }

    public Integer getSituation() {
        return situation;
    }

    public void setSituation(Integer situation) {
        this.situation = situation;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<PlayerDetails> getPlayers() {
        return players;
    }

    public String[] getPlayersAsString() {
        String[] usernames = new String[players.size()];

        int i = 0;
        for (PlayerDetails player : this.players) {
            usernames[i] = player.getUsername();
            i++;
        }

        return usernames;
    }

    public void setPlayers(List<PlayerDetails> players) {
        this.players = players;
    }

    public Boolean getClosedLobby() {
        return closedLobby;
    }

    public void setClosedLobby(Boolean closedLobby) {
        this.closedLobby = closedLobby;
    }

    public Integer getGameId() {
        return gameId;
    }

    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getGameServerAddr() {
        return gameServerAddr;
    }

    public void setGameServerAddr(String gameServerAddr) {
        this.gameServerAddr = gameServerAddr;
    }

    public Integer getGameServerPort() {
        return gameServerPort;
    }

    public void setGameServerPort(Integer gameServerPort) {
        this.gameServerPort = gameServerPort;
    }

    public Long getGameSeed() {
        return this.gameSeed;
    }
    public void setGameSeed(Long gameSeed) {
        this.gameSeed = gameSeed;
    }

    @Override
    public String toString() {
        return this.message;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(situation);
        dest.writeValue(message);
        dest.writeList(players);
        dest.writeValue(closedLobby);
        dest.writeValue(gameId);
        dest.writeValue(userId);
        dest.writeValue(gameServerAddr);
        dest.writeValue(gameServerPort);
    }

    public int describeContents() {
        return 0;
    }
}
