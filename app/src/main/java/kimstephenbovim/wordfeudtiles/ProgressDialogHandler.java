package kimstephenbovim.wordfeudtiles;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Handler;

import kimstephenbovim.wordfeudtiles.domain.User;
import kimstephenbovim.wordfeudtiles.rest.RestClient;

import static android.os.Looper.getMainLooper;
import static java.lang.System.currentTimeMillis;

public class ProgressDialogHandler {

    public static ProgressDialogHandler shared = new ProgressDialogHandler();
    private Handler handler = new Handler(getMainLooper());
    private ProgressDialog progressDialog;
    private long restCallStarted, progressDialogShown;

    private void show(final Activity activity, final String message) {
        handler.removeCallbacksAndMessages(null);
        final boolean isShowing = progressDialog != null && progressDialog.isShowing();

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (progressDialog != null && isShowing) {
                    progressDialog.setMessage(message);
                    progressDialogShown = currentTimeMillis();
                } else {
                    progressDialog = new ProgressDialog(activity);
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setMessage(message);
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            progressDialog.show();
                            progressDialogShown = currentTimeMillis();
                        }
                    }, Math.max(500, currentTimeMillis() - restCallStarted));
                }
            }
        });

    }

    public void cancel() {
        handler.removeCallbacksAndMessages(null);
        handler.postDelayed(new Runnable() {
            public void run() {
                progressDialog.dismiss();
            }
        }, progressDialog.isShowing() ? Math.max(0, 500 - (currentTimeMillis() - progressDialogShown)) : 0);
    }

    public void dismiss() {
        handler.removeCallbacksAndMessages(null);
        progressDialog.dismiss();
    }

    public void getGames(Activity activity, boolean attemptRelogin) {
        RestClient.getGames(attemptRelogin);
        restCallStarted = currentTimeMillis();
        show(activity, Texts.shared.getText("pleaseWait"));
    }

    public void getGame(Activity activity, long gameId, boolean attemptRelogin) {
        RestClient.getGame(gameId, attemptRelogin);
        restCallStarted = currentTimeMillis();
        show(activity, Texts.shared.getText("pleaseWait"));
    }

    public void login(Activity activity, User user) {
        final String loginValue = "email".equals(user.getLoginMethod())
                ? user.getEmail()
                : user.getUsername();

        WFTiles.instance.setLastAttemptedLogin(user);
        login(activity, user.getLoginMethod(), loginValue, user.getPassword());
    }

    public void login(Activity activity, String loginMethod, String loginValue, String password) {
        RestClient.login(loginMethod, loginValue, password);
        restCallStarted = currentTimeMillis();
        show(activity, Texts.shared.getText("loggingIn"));
    }
}
