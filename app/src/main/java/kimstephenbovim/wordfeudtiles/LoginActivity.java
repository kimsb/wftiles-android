package kimstephenbovim.wordfeudtiles;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import kimstephenbovim.wordfeudtiles.domain.User;
import kimstephenbovim.wordfeudtiles.event.LoginEvent;

import static kimstephenbovim.wordfeudtiles.Constants.MESSAGE_NO_LOGIN_SUGGESTION;
import static kimstephenbovim.wordfeudtiles.Constants.MESSAGE_SKIP_LOGIN;

public class LoginActivity extends AppCompatActivity {

    private UserLoginTask authTask = null;
    private EditText usernameView;
    private EditText passwordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (getIntent().getBooleanExtra(MESSAGE_SKIP_LOGIN, true)
                && WFTiles.instance.getLoggedInUser() != null) {
            System.out.println("Har lagret bruker, går rett til GameList");
            LoginActivity loginActivity = LoginActivity.this;
            Intent intent = new Intent(loginActivity, GameListActivity.class);
            loginActivity.startActivity(intent);
            finish();
        }

        setTheme(R.style.CustomToolbarStyle);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        Toolbar toolbar = findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Wordfeud Tiles");
        }

        // Set up the login form.
        TextView loginInfo = findViewById(R.id.loginInfo);
        loginInfo.setText(Texts.shared.getText("loginInfo"));

        usernameView = findViewById(R.id.login_username);
        usernameView.setImeActionLabel(Texts.shared.getText("next"), 5);
        passwordView = findViewById(R.id.login_password);
        passwordView.setImeActionLabel(Texts.shared.getText("login"), 6);

        final User user = WFTiles.instance.getLastAttemptedLogin();
        if (getIntent().getBooleanExtra(MESSAGE_NO_LOGIN_SUGGESTION, false) || user == null) {
            usernameView.setHint(Texts.shared.getText("usernameEmail"));
            passwordView.setHint(Texts.shared.getText("password"));
            usernameView.requestFocus();
        } else {
            usernameView.setText("email".equals(user.getLoginMethod()) ? user.getEmail() : user.getUsername());
            passwordView.setText(user.getPassword());
            passwordView.requestFocus();
        }

        passwordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button signInButton = findViewById(R.id.email_sign_in_button);
        signInButton.setText(Texts.shared.getText("login"));
        signInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
    }

    private void attemptLogin() {
        if (authTask != null) {
            return;
        }

        String username = usernameView.getText().toString();
        String password = passwordView.getText().toString();

        if ("".equals(username)) {
            alert(Texts.shared.getText("unknownUsername"));
            return;
        }

        if ("".equals(password)) {
            alert(Texts.shared.getText("wrongPassword"));
            return;
        }

        // Show a progress spinner, and kick off a background task to
        // perform the user login attempt.
        authTask = new UserLoginTask(username, password);
        authTask.execute((Void) null);
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            System.out.println("Attempting login");

            ProgressDialogHandler.shared.login(LoginActivity.this, mEmail.contains("@") ? "email" : "username", mEmail, mPassword);

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            authTask = null;

            if (!success) {
                System.out.println("authTask feiler!");
            }
        }

        @Override
        protected void onCancelled() {
            authTask = null;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LoginEvent loginEvent) {
        ProgressDialogHandler.shared.cancel();
        switch (loginEvent.getLoginResult()) {
            case OK:
                System.out.println("Jippi! skal åpne games");
                LoginActivity loginActivity = LoginActivity.this;
                Intent intent = new Intent(loginActivity, GameListActivity.class);
                loginActivity.startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                break;
            case WRONG_PASSWORD:
                System.out.println("Feil passord");
                alert(Texts.shared.getText("wrongPassword"));
                break;
            case UNKNOWN_USER:
                System.out.println("Ukjent bruker");
                alert(Texts.shared.getText("unknownUsername"));
                break;
            case UNKNOWN_EMAIL:
                System.out.println("Ukjent epost");
                alert(Texts.shared.getText("unknownEmail"));
                break;
            case FAILED:
                System.out.println("Login feiler");
                alert(isOnline() ? "" : Texts.shared.getText("connectionError"));
        }
    }

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    private void alert(final String errorMessage) {
        new AlertDialog.Builder(LoginActivity.this)
                .setTitle(Texts.shared.getText("loginFailed"))
                .setMessage(errorMessage)
                .setPositiveButton(Texts.shared.getText("ok"), null)
                .create()
                .show();
    }
}

