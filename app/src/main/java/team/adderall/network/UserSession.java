package team.adderall.network;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserSession
{
    public final static String SESSION_TOKEN_NAME = "x-session-token";

    @SerializedName(SESSION_TOKEN_NAME)
    @Expose
    private String token;

    public UserSession() {
        this.token = "";
    }

    public UserSession(String token) {
        this.token = token;
    }

    public UserSession(Bundle bundle) {
        this.token = bundle == null ? "" : bundle.getString(SESSION_TOKEN_NAME);
    }

    public String getToken() {
        return token;
    }
    public void setToken(@NonNull String t) {
        token = t;
    }

    public boolean isEmpty() {
        return this.token.equals("");
    }
}
