package team.adderall.network;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PlayerDetails
        implements
        Serializable,
        Parcelable
{

    @SerializedName("username")
    @Expose
    private String username;
    public final static Parcelable.Creator<PlayerDetails> CREATOR = new Creator<PlayerDetails>() {

        @SuppressWarnings({"unchecked"})
        public PlayerDetails createFromParcel(Parcel in) {
            return new PlayerDetails(in);
        }

        public PlayerDetails[] newArray(int size) {
            return (new PlayerDetails[size]);
        }
    };

    private final static long serialVersionUID = -4853944408465078815L;

    protected PlayerDetails(Parcel in) {
        this.username = ((String) in.readValue((String.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     *
     */
    public PlayerDetails() {
    }

    /**
     *
     * @param username
     */
    public PlayerDetails(String username) {
        super();
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return this.username;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(username);
    }

    public int describeContents() {
        return 0;
    }

}