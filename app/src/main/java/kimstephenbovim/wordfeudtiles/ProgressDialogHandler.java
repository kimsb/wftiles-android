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
    private boolean startedDismissing;
    private Handler handler = new Handler(getMainLooper());
    private ProgressDialog progressDialog;
    private long restCallStarted, progressDialogShown;
    private String currentActivityId;

    private void show(final String activityId, final Activity activity, final String message) {
        currentActivityId = activityId;
        startedDismissing = false;
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
        handler.postDelayed(new Runnable() {
            public void run() {
                if (!startedDismissing && progressDialog != null) {
                    startedDismissing = true;
                    handler.removeCallbacksAndMessages(null);
                    progressDialog.dismiss();
                }
            }
        }, progressDialog.isShowing() ? Math.max(0, 500 - (currentTimeMillis() - progressDialogShown)) : 0);
    }

    public void dismiss(String activityId) {
        if (activityId.equals(currentActivityId)
        && !startedDismissing && progressDialog != null) {
            startedDismissing = true;
            handler.removeCallbacksAndMessages(null);
            progressDialog.dismiss();
        }
    }

    public void getGames(String activityId, Activity activity, boolean attemptRelogin) {
        RestClient.getGames(attemptRelogin);
        restCallStarted = currentTimeMillis();
        show(activityId, activity, Texts.shared.getText("pleaseWait"));
    }

    public void getGame(String activityId, Activity activity, long gameId, boolean attemptRelogin) {
        RestClient.getGame(gameId, attemptRelogin);
        restCallStarted = currentTimeMillis();
        show(activityId, activity, Texts.shared.getText("pleaseWait"));
    }

    public void login(String activityId, Activity activity, User user) {
        final String loginValue = "email".equals(user.getLoginMethod())
                ? user.getEmail()
                : user.getUsername();

        WFTiles.instance.setLastAttemptedLogin(user);
        login(activityId, activity, user.getLoginMethod(), loginValue, user.getPassword());
    }

    public void login(String activityId, Activity activity, String loginMethod, String loginValue, String password) {
        RestClient.login(loginMethod, loginValue, password);
        restCallStarted = currentTimeMillis();
        show(activityId, activity, Texts.shared.getText("loggingIn"));
    }
}
