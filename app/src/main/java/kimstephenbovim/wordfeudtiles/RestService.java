package kimstephenbovim.wordfeudtiles;

import kimstephenbovim.wordfeudtiles.pojo.GameResponse;
import kimstephenbovim.wordfeudtiles.pojo.GamesResponse;
import kimstephenbovim.wordfeudtiles.pojo.LoginResponse;
import kimstephenbovim.wordfeudtiles.pojo.LoginWithEmailBody;
import kimstephenbovim.wordfeudtiles.pojo.LoginWithUsernameBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RestService {

    @Headers({"Accept: application/json",
            "Content-Type: application/json",
            "Cache-Control: no-cahce"})
    @POST("user/login/")
    Call<LoginResponse> loginWithUsername(@Body LoginWithUsernameBody loginWithUsernameBody);

    @Headers({"Accept: application/json",
            "Content-Type: application/json",
            "Cache-Control: no-cahce"})
    @POST("user/login/email/")
    Call<LoginResponse> loginWithEmail(@Body LoginWithEmailBody loginWithEmailBody);

    @Headers({"Accept: application/json",
            "Content-Type: application/json",
            "Cache-Control: no-cahce"})
    @GET("user/games/")
    Call<GamesResponse> getGames();

    @Headers({"Accept: application/json",
            "Content-Type: application/json",
            "Cache-Control: no-cahce"})
    @GET("game/{gameId}/")
    Call<GameResponse> getGame(@Path("gameId") long gameId);
}
