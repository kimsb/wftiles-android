package kimstephenbovim.wordfeudtiles.rest;

import org.greenrobot.eventbus.EventBus;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kimstephenbovim.wordfeudtiles.WFTiles;
import kimstephenbovim.wordfeudtiles.domain.Game;
import kimstephenbovim.wordfeudtiles.domain.User;
import kimstephenbovim.wordfeudtiles.event.GameLoadedEvent;
import kimstephenbovim.wordfeudtiles.event.GamesLoadedEvent;
import kimstephenbovim.wordfeudtiles.event.LoginEvent;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

import static java.util.concurrent.TimeUnit.SECONDS;
import static kimstephenbovim.wordfeudtiles.Mapper.mapToGame;
import static kimstephenbovim.wordfeudtiles.Mapper.mapToGames;
import static kimstephenbovim.wordfeudtiles.Mapper.mapToUser;
import static kimstephenbovim.wordfeudtiles.event.LoginResult.FAILED;
import static kimstephenbovim.wordfeudtiles.event.LoginResult.OK;
import static kimstephenbovim.wordfeudtiles.event.LoginResult.UNKNOWN_EMAIL;
import static kimstephenbovim.wordfeudtiles.event.LoginResult.UNKNOWN_USER;
import static kimstephenbovim.wordfeudtiles.event.LoginResult.WRONG_PASSWORD;

public class RestClient {

    private static final String BASE_URL = "https://api.wordfeud.com/wf/";
    private static RestService restService = getClient().create(RestService.class);

    private static Retrofit getClient() {
        CookieJar cookieJar = new CookieJar() {
            private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();

            @Override
            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                cookieStore.put(url.host(), cookies);
            }

            @Override
            public List<Cookie> loadForRequest(HttpUrl url) {
                List<Cookie> cookies = cookieStore.get(url.host());
                return cookies != null ? cookies : new ArrayList<Cookie>();
            }
        };
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(10, SECONDS)
                .connectTimeout(10, SECONDS)
                .cookieJar(cookieJar)
                .build();

        return new retrofit2.Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .client(okHttpClient)
                .build();
    }

    private static void reLogin() {
        System.out.println("RE-logging in");
        final User user = WFTiles.instance.getUser();

        final String loginValue = "email".equals(user.getLoginMethod())
                ? user.getEmail()
                : user.getUsername();

        login(user.getLoginMethod(), loginValue, user.getPassword());
    }

    public static void login(final String loginMethod, final String loginValue, final String password) {
        final Call<LoginResponse> loginResponseCall = "email".equals(loginMethod)
                ? restService.loginWithEmail(new LoginWithEmailBody(loginValue, getSha1Hex(password)))
                : restService.loginWithUsername(new LoginWithUsernameBody(loginValue, getSha1Hex(password)));

        loginResponseCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if ("error".equals(response.body().getStatus())) {
                    switch (response.body().getLoginContent().getType()) {
                        case "unknown_username":
                            EventBus.getDefault().post(new LoginEvent(UNKNOWN_USER));
                            break;
                        case "unknown_email":
                            EventBus.getDefault().post(new LoginEvent(UNKNOWN_EMAIL));
                            break;
                        case "wrong_password":
                            EventBus.getDefault().post(new LoginEvent(WRONG_PASSWORD));
                            break;
                    }
                } else {
                    EventBus.getDefault().post(new LoginEvent(OK));
                    WFTiles.instance.setUser(mapToUser(response.body().getLoginContent(), loginMethod, password));
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                EventBus.getDefault().post(new LoginEvent(FAILED));
            }
        });
    }

    public static void getGames(final boolean attemptRelogin) {
        restService.getGames().enqueue(new Callback<GamesResponse>() {
            @Override
            public void onResponse(Call<GamesResponse> call, Response<GamesResponse> response) {

                if ("error".equals(response.body().getStatus())) {
                    System.out.println("getGames feiler med: " + response.body().getGamesContent().getType());
                    if ("login_required".equals(response.body().getGamesContent().getType())) {
                        if (attemptRelogin) {
                            reLogin();
                        } else {
                            EventBus.getDefault().post(new GamesLoadedEvent(null));
                        }
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
                EventBus.getDefault().post(new GamesLoadedEvent(null));
            }
        });
    }

    public static void getGame(final long gameId, final boolean attemptRelogin) {
        restService.getGame(gameId).enqueue(new Callback<GameResponse>() {
            @Override
            public void onResponse(Call<GameResponse> call, Response<GameResponse> response) {

                if ("error".equals(response.body().getStatus())) {
                    System.out.println("getGame feiler med: " + response.body().getGameContent().getType());
                    if ("login_required".equals(response.body().getGameContent().getType())) {
                            if (attemptRelogin) {
                            reLogin();
                        } else {
                                EventBus.getDefault().post(new GameLoadedEvent(null));
                        }

                    }
                } else {
                    System.out.println("getGame success");
                    Game game = mapToGame(response.body().getGameContent().getGameDTO());

                    WFTiles.instance.setGame(game);
                    EventBus.getDefault().post(new GameLoadedEvent(game));
                }
            }

            @Override
            public void onFailure(Call<GameResponse> call, Throwable t) {
                EventBus.getDefault().post(new GameLoadedEvent(null));
            }
        });
    }

    private static String getSha1Hex(String password) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("SHA-1");
            messageDigest.update((password + "JarJarBinks9").getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ignored) { }
        assert messageDigest != null;
        byte[] bytes = messageDigest.digest();
        StringBuilder buffer = new StringBuilder();
        for (byte b : bytes) {
            buffer.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        }
        return buffer.toString();
    }

}
