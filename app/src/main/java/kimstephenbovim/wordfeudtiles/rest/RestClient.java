package kimstephenbovim.wordfeudtiles.rest;

import org.greenrobot.eventbus.EventBus;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.List;

import kimstephenbovim.wordfeudtiles.WFTiles;
import kimstephenbovim.wordfeudtiles.domain.Game;
import kimstephenbovim.wordfeudtiles.event.GameLoadedEvent;
import kimstephenbovim.wordfeudtiles.event.GamesLoadedEvent;
import kimstephenbovim.wordfeudtiles.event.LoginEvent;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

import static kimstephenbovim.wordfeudtiles.Mapper.mapToGame;
import static kimstephenbovim.wordfeudtiles.Mapper.mapToGames;
import static kimstephenbovim.wordfeudtiles.Mapper.mapToUser;

public class RestClient {

    private static final String BASE_URL = "https://api.wordfeud.com/wf/";
    private static RestService restService = getClient().create(RestService.class);

    private static Retrofit getClient() {
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        OkHttpClient okHttpClient = new OkHttpClient.Builder().cookieJar(new JavaNetCookieJar(cookieManager)).build();

        return new retrofit2.Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .client(okHttpClient)
                .build();
    }

    public static void login() {

        Call<LoginResponse> loginResponseCall = restService.loginWithUsername(new LoginWithUsernameBody("furtoad", "cb95625ce12dfcf764915dceb4bf857474eeb01d"));
        //Call<LoginResponse> loginResponseCall = restService.loginWithEmail(new LoginWithEmailBody("mail", "cb95625ce12dfcf764915dceb4bf857474eeb01d"));

        loginResponseCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                System.out.println("login success!");
                WFTiles.instance.setUser(mapToUser(response.body().getLoginContent(), "loginMethod", "password"));

                EventBus.getDefault().post(new LoginEvent());
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                //TODO: feilhåndtering ved feil passord/brukernavn
                System.out.println("login failure...");
            }
        });
    }

    public static void getGames() {
        restService.getGames().enqueue(new Callback<GamesResponse>() {
            @Override
            public void onResponse(Call<GamesResponse> call, Response<GamesResponse> response) {

                if ("error".equals(response.body().getStatus())) {
                    System.out.println("getGames feiler med: " + response.body().getGamesContent().getType());
                    if ("login_required".equals(response.body().getGamesContent().getType())) {
                        login();
                    }
                } else {

                    System.out.println("getGames success!");
                    List<Game> games = mapToGames(response.body().getGamesContent().getGames());

                    WFTiles.instance.setGames(games);
                    EventBus.getDefault().post(new GamesLoadedEvent(games));
                }
            }

            @Override
            public void onFailure(Call<GamesResponse> call, Throwable t) {
                //TODO: feilhåndtering ved krav om login
                System.out.println("getGames failure...");
            }
        });
    }

    public static void getGame(final long gameId) {
        restService.getGame(gameId).enqueue(new Callback<GameResponse>() {
            @Override
            public void onResponse(Call<GameResponse> call, Response<GameResponse> response) {

                Game game = mapToGame(response.body().getGameContent().getGameDTO());

                WFTiles.instance.setGame(game);
                EventBus.getDefault().post(new GameLoadedEvent(game));
                System.out.println("getGame success");
            }

            @Override
            public void onFailure(Call<GameResponse> call, Throwable t) {
                //TODO: feilhåndtering ved krav om login
                System.out.println("getGame failure...");
            }
        });
    }

}
