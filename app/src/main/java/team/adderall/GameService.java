package team.adderall;

import retrofit2.Call;
import retrofit2.http.*;

public interface GameService {
    @POST("auth")
    Call<JSend<UserSession>> authenticate(@Body PlayerDetails details);

    @GET("game")
    Call<JSend<PlayerStatus>> joinAGame(@Header(UserSession.SESSION_TOKEN_NAME) String token);
}
