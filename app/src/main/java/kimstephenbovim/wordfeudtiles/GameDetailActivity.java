package kimstephenbovim.wordfeudtiles;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import static kimstephenbovim.wordfeudtiles.Constants.MESSAGE_GAME_ID;
import static kimstephenbovim.wordfeudtiles.Constants.MESSAGE_IS_TWOPANE;
import static kimstephenbovim.wordfeudtiles.Constants.MESSAGE_OPPONENT_NAME;
import static kimstephenbovim.wordfeudtiles.PreferencesMenu.initMenu;
import static kimstephenbovim.wordfeudtiles.PreferencesMenu.onClicked;

public class GameDetailActivity extends AppCompatActivity {

    private Menu menu;
    private GameDetailFragment gameDetailFragment;
    public SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_detail);
        Toolbar toolbar = findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getIntent().getStringExtra(MESSAGE_OPPONENT_NAME));
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.addOnMenuVisibilityListener(new ActionBar.OnMenuVisibilityListener() {

                @Override
                public void onMenuVisibilityChanged(boolean isVisible) {
                    if (isVisible) {
                        initMenu(menu);
                    }
                }
            });
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putLong(MESSAGE_GAME_ID,
                    getIntent().getLongExtra(MESSAGE_GAME_ID, 0));
            arguments.putBoolean(MESSAGE_IS_TWOPANE, getIntent().getBooleanExtra(MESSAGE_IS_TWOPANE, false));
            GameDetailFragment fragment = new GameDetailFragment();
            fragment.setArguments(arguments);
            gameDetailFragment = fragment;
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.game_detail_container, fragment, "her_er_min_tag")
                    .commit();
        }

        swipeRefreshLayout = findViewById(R.id.swipeRefreshDetail);
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        ProgressDialogHandler.shared.getGame(GameDetailActivity.this, getIntent().getLongExtra(MESSAGE_GAME_ID, 0), true);
                    }
                }
        );


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        initMenu(menu);

        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpTo(this, new Intent(this, GameListActivity.class));
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            return true;
        }
        onClicked(item, this);
        if (gameDetailFragment != null) {
            gameDetailFragment.updateView();
        }
        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        ProgressDialogHandler.shared.dismiss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ProgressDialogHandler.shared.dismiss();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        ProgressDialogHandler.shared.dismiss();
    }
}
