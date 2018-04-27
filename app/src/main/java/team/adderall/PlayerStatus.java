package team.adderall;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PlayerStatus implements Serializable, Parcelable
{

    @SerializedName("situation")
    @Expose
    private Integer situation;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("players")
    @Expose
    private Integer players;
    @SerializedName("closed_lobby")
    @Expose
    private Boolean closedLobby;
    @SerializedName("start_time")
    @Expose
    private Integer startTime;
    @SerializedName("game_id")
    @Expose
    private Integer gameId;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
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

    }
            ;
    private final static long serialVersionUID = -2131281992101377437L;

    protected PlayerStatus(Parcel in) {
        this.situation = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
        this.players = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.closedLobby = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.startTime = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.gameId = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.userId = ((Integer) in.readValue((Integer.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     *
     */
    public PlayerStatus() {
    }

    /**
     *
     * @param startTime
     * @param message
     * @param closedLobby
     * @param userId
     * @param gameId
     * @param players
     * @param situation
     */
    public PlayerStatus(Integer situation, String message, Integer players, Boolean closedLobby, Integer startTime, Integer gameId, Integer userId) {
        super();
        this.situation = situation;
        this.message = message;
        this.players = players;
        this.closedLobby = closedLobby;
        this.startTime = startTime;
        this.gameId = gameId;
        this.userId = userId;
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

    public Integer getPlayers() {
        return players;
    }

    public void setPlayers(Integer players) {
        this.players = players;
    }

    public Boolean getClosedLobby() {
        return closedLobby;
    }

    public void setClosedLobby(Boolean closedLobby) {
        this.closedLobby = closedLobby;
    }

    public Integer getStartTime() {
        return startTime;
    }

    public void setStartTime(Integer startTime) {
        this.startTime = startTime;
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

    @Override
    public String toString() {
        return this.message;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(situation);
        dest.writeValue(message);
        dest.writeValue(players);
        dest.writeValue(closedLobby);
        dest.writeValue(startTime);
        dest.writeValue(gameId);
        dest.writeValue(userId);
    }

    public int describeContents() {
        return 0;
    }

}
