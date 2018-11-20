package kimstephenbovim.wordfeudtiles;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
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
import kimstephenbovim.wordfeudtiles.rest.RestClient;

import static kimstephenbovim.wordfeudtiles.Constants.MESSAGE_SKIP_LOGIN;

public class LoginActivity extends AppCompatActivity {

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private EditText usernameView;
    private EditText passwordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (getIntent().getBooleanExtra(MESSAGE_SKIP_LOGIN, true)
            && WFTiles.instance.getUser() != null) {
            System.out.println("Har lagret bruker, går rett til GameList");
            Context context = this.getApplicationContext();
            Intent intent = new Intent(context, GameListActivity.class);
            context.startActivity(intent);
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
        passwordView = findViewById(R.id.login_password);
        final User user = WFTiles.instance.getUser();
        if (user == null) {
            usernameView.setHint(Texts.shared.getText("usernameEmail"));
            passwordView.setHint(Texts.shared.getText("password"));
        } else {
            usernameView.setText("email".equals(user.getLoginMethod()) ? user.getEmail() : user.getUsername());
            passwordView.setText(user.getPassword());
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

        mLoginFormView = findViewById(R.id.login_form);
        //mProgressView = findViewById(R.id.login_progress);
    }

    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        String email = usernameView.getText().toString();
        String password = passwordView.getText().toString();

        // Show a progress spinner, and kick off a background task to
        // perform the user login attempt.
        mAuthTask = new UserLoginTask(email, password);
        mAuthTask.execute((Void) null);
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

            RestClient.login(mEmail.contains("@") ? "email" : "username", mEmail, mPassword);

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;

            if (success) {
                System.out.println("success!");
            } else {
                passwordView.setError(getString(R.string.error_incorrect_password));
                passwordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
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

    //TODO tilbakemelding om feil passord / bruker / timeout
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onMessageEvent(LoginEvent loginEvent) {
        switch (loginEvent.getLoginResult()) {
            case OK:
                System.out.println("Jippi! skal åpne games");
                Context context = this.getApplicationContext();
                Intent intent = new Intent(context, GameListActivity.class);
                context.startActivity(intent);
                break;
            case WRONG_PASSWORD:
                System.out.println("Feil passord");
                break;
            case UNKNOWN_USER:
                System.out.println("Ukjent bruker");
            case FAILED:
                System.out.println("Login feiler");
        }
    }
}

