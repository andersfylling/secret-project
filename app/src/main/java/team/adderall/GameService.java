package team.adderall;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface GameService {
    @GET("auth")
    Call<JSend<UserSession>> authenticate();

    @GET("game")
    Call<JSend<PlayerStatus>> joinAGame(@Header(UserSession.SESSION_TOKEN_NAME) String token);
}
