package kimstephenbovim.wordfeudtiles;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import kimstephenbovim.wordfeudtiles.domain.Game;
import kimstephenbovim.wordfeudtiles.domain.GameRow;
import kimstephenbovim.wordfeudtiles.event.GamesLoadedEvent;
import kimstephenbovim.wordfeudtiles.event.LoginEvent;

import static kimstephenbovim.wordfeudtiles.Constants.MESSAGE_GAME_ID;
import static kimstephenbovim.wordfeudtiles.Constants.MESSAGE_IS_TWOPANE;
import static kimstephenbovim.wordfeudtiles.Constants.MESSAGE_OPPONENT_NAME;
import static kimstephenbovim.wordfeudtiles.Constants.MESSAGE_SHOW_TWOPANE_GAME;
import static kimstephenbovim.wordfeudtiles.Constants.MESSAGE_SKIP_LOGIN;
import static kimstephenbovim.wordfeudtiles.PreferencesMenu.initMenu;
import static kimstephenbovim.wordfeudtiles.PreferencesMenu.onClicked;
import static kimstephenbovim.wordfeudtiles.domain.GameRowType.HEADER;
import static kimstephenbovim.wordfeudtiles.event.LoginResult.OK;

public class GameListActivity extends AppCompatActivity {

    private boolean isTwoPane;
    private List<Game> games;
    private boolean isCreated;
    private String appbarTitleSpacing;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Long selectedGameId;
    private Menu menu;
    GameDetailFragment gameDetailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_list);

        Toolbar toolbar = findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);

        if (findViewById(R.id.game_detail_container) != null) {
            isTwoPane = true;
            setAppbarTitleSpacing();
        }

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            if (WFTiles.instance.getUser() != null) {
                if (isTwoPane && gameDetailFragment != null) {
                    actionBar.setTitle(WFTiles.instance.getUser().presentableFullUsername()
                            + appbarTitleSpacing
                            + gameDetailFragment.getOpponentUsername());
                } else {
                    actionBar.setTitle(WFTiles.instance.getUser().presentableFullUsername());
                }
            }
            actionBar.setDisplayHomeAsUpEnabled(true);
            Glide.with(this)
                    .load(WFTiles.instance.getUser().getAvatarRoot() + "/80/" + WFTiles.instance.getUser().getId())
                    .apply(RequestOptions.circleCropTransform())
                    .apply(RequestOptions.placeholderOf(R.drawable.circle))
                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource,
                                                    @Nullable Transition<? super Drawable> transition) {
                            actionBar.setHomeAsUpIndicator(resource);
                        }
                    });

            actionBar.addOnMenuVisibilityListener(new ActionBar.OnMenuVisibilityListener() {

                @Override
                public void onMenuVisibilityChanged(boolean isVisible) {
                    if (isVisible) {
                        initMenu(menu);
                    }
                }
            });
        }

        setupRecyclerView(WFTiles.instance.getGames());

        ProgressDialogHandler.shared.getGames(this, true);

        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        ProgressDialogHandler.shared.getGames(GameListActivity.this, true);
                        if (isTwoPane && selectedGameId != null) {
                            ProgressDialogHandler.shared.getGame(GameListActivity.this, selectedGameId, false);
                        }
                    }
                }
        );
        long showGameId = getIntent().getLongExtra(MESSAGE_SHOW_TWOPANE_GAME, 0);
        if (isTwoPane && showGameId != 0 && games != null) {
            for (Game game : games) {
                if (game.getId() == showGameId) {
                    showTwoPaneGame(game);
                    break;
                }
            }
        }
        if (!isTwoPane && gameDetailFragment!= null && games != null) {
            for (Game game : games) {
                if (game.getId() == gameDetailFragment.gameId) {
                    showGame(game);
                    break;
                }
            }
        }
    }

    private void setAppbarTitleSpacing() {
        Paint paint = new Paint();
        paint.setTextSize(getResources().getDimension(R.dimen.appbar_textsize));
        float playerNameWidth = paint.measureText(WFTiles.instance.getUser().presentableFullUsername());
        float spaceWidth = paint.measureText(" ");

        float opponentPosition = getResources().getDimension(R.dimen.twopane_opponent_position);
        float spaceCount = (opponentPosition - playerNameWidth) / spaceWidth;

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < spaceCount; i++) {
            builder.append(" ");
        }
        appbarTitleSpacing = builder.append("     ").toString();
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
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra(MESSAGE_SKIP_LOGIN, false);
            NavUtils.navigateUpTo(this, intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            return true;
        }
        onClicked(item, this);
        setupRecyclerView(games);
        if (gameDetailFragment != null) {
            gameDetailFragment.updateView();
        }
        return false;
    }

    public void setupRecyclerView(List<Game> games) {
        this.games = games;
        RecyclerView recyclerView = findViewById(R.id.game_list);
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, Mapper.mapGamesToGameRows(games), isTwoPane));
    }

    void showTwoPaneGame(Game game) {
        Bundle arguments = new Bundle();
        arguments.putLong(MESSAGE_GAME_ID, game.getId());
        arguments.putString(MESSAGE_OPPONENT_NAME, game.getOpponent().presentableUsername());
        arguments.putBoolean(MESSAGE_IS_TWOPANE, true);
        GameDetailFragment fragment = new GameDetailFragment();
        fragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.game_detail_container, fragment, "her_er_min_tag")
                .commit();

        getSupportActionBar().setTitle(WFTiles.instance.getUser().presentableFullUsername()
                + appbarTitleSpacing
                + game.getOpponent().presentableUsername());
        ProgressDialogHandler.shared.getGames(this, false);
    }

    void showGame(Game game) {
        Intent intent = new Intent(this, GameDetailActivity.class);
        intent.putExtra(MESSAGE_GAME_ID, game.getId());
        intent.putExtra(MESSAGE_OPPONENT_NAME, game.getOpponent().presentableUsername());
        intent.putExtra(MESSAGE_IS_TWOPANE, false);
        startActivity(intent);
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private final GameListActivity parentActivity;
        private final List<GameRow> gameRows;
        private final boolean isTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GameRow gameRow = (GameRow) view.getTag();
                Game game = gameRow.getGame();

                if (isTwoPane) {
                    parentActivity.selectedGameId = game.getId();
                    parentActivity.showTwoPaneGame(game);
                } else {
                    parentActivity.showGame(game);
                    parentActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            }
        };

        SimpleItemRecyclerViewAdapter(GameListActivity parent,
                                      List<GameRow> gameRows,
                                      boolean twoPane) {
            this.gameRows = gameRows;
            parentActivity = parent;
            isTwoPane = twoPane;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == HEADER.ordinal()) {
                return new HeaderViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.game_list_header, parent, false));
            }
            return new GameViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.game_list_content, parent, false));
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
            GameRow gameRow = gameRows.get(position);
            if (gameRow.getGameRowType().equals(HEADER)) {
                HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
                headerViewHolder.headerText.setText(gameRow.getHeaderTitle());
            } else {
                GameViewHolder gameViewHolder = (GameViewHolder) holder;
                Game game = gameRow.getGame();

                Glide.with(this.parentActivity)
                        .load(WFTiles.instance.getUser().getAvatarRoot() + "/80/" + game.getOpponent().getId())
                        .apply(RequestOptions.circleCropTransform())
                        .apply(RequestOptions.placeholderOf(R.drawable.circle))
                        .into(gameViewHolder.opponentImageView);
                int diff = game.getPlayer().getScore() - game.getOpponent().getScore();
                String diffText = (diff > 0 ? "+" : "") + diff;
                gameViewHolder.diffLabel.setText(diffText);
                if (diff > 0) {
                    gameViewHolder.diffLabel.setBackgroundResource(R.drawable.green_diff);
                } else if (diff < 0) {
                    gameViewHolder.diffLabel.setBackgroundResource(R.drawable.red_diff);
                } else {
                    gameViewHolder.diffLabel.setBackgroundResource(R.drawable.yellow_diff);
                }
                gameViewHolder.opponentText.setText(game.getOpponent().presentableUsername());
                gameViewHolder.languageText.setText(Texts.shared.getGameLanguage(game.getRuleset()));
                gameViewHolder.scoreText.setText(String.format("%d - %d", game.getPlayer().getScore(), game.getOpponent().getScore()));
                gameViewHolder.lastMoveText.setText(game.getLastMoveText());

                holder.itemView.setOnClickListener(mOnClickListener);
            }
            holder.itemView.setTag(gameRows.get(position));
        }

        @Override
        public int getItemViewType(int position) {
            return gameRows.get(position).getGameRowType().ordinal();
        }

        @Override
        public int getItemCount() {
            return gameRows.size();
        }

        class HeaderViewHolder extends RecyclerView.ViewHolder {
            final TextView headerText;

            HeaderViewHolder(View view) {
                super(view);
                headerText = view.findViewById(R.id.headerText);
            }
        }

        class GameViewHolder extends RecyclerView.ViewHolder {
            final ImageView opponentImageView;
            final TextView diffLabel;
            final TextView opponentText;
            final TextView languageText;
            final TextView scoreText;
            final TextView lastMoveText;

            GameViewHolder(View view) {
                super(view);
                opponentImageView = view.findViewById(R.id.opponentImageView);
                diffLabel = view.findViewById(R.id.diffLabel);
                opponentText = view.findViewById(R.id.opponentText);
                languageText = view.findViewById(R.id.languageText);
                scoreText = view.findViewById(R.id.scoreText);
                lastMoveText = view.findViewById(R.id.lastMoveText);
            }
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

    @Override
    public void onResume() {
        super.onResume();
        if (isCreated) {
            ProgressDialogHandler.shared.getGames(this, true);
            if (isTwoPane && selectedGameId != null) {
                ProgressDialogHandler.shared.getGame(this, selectedGameId, false);
            }
        } else {
            isCreated = true;
        }
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

    private void stopRefreshing() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(GamesLoadedEvent gamesLoadedEvent) {
        ProgressDialogHandler.shared.cancel();
        stopRefreshing();
        if (gamesLoadedEvent.getGames() == null) {
            alert(isOnline() ? "" : Texts.shared.getText("connectionError"));
            return;
        }

        //TODO refactor game-comparings + all compare-logic outside main thread
        List<Game> loadedGames = gamesLoadedEvent.getGames();
        setupRecyclerView(loadedGames);
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onMessageEvent(LoginEvent loginEvent) {
        if (loginEvent.getLoginResult() == OK) {
            ProgressDialogHandler.shared.getGames(this, false);
        } else {
            //TODO login har feilet, kan dette skje?
            System.out.println("Login feiler");
        }
    }

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    private void alert(final String errorMessage) {
        new AlertDialog.Builder(this)
                .setTitle(Texts.shared.getText("loadingFailed"))
                .setMessage(errorMessage)
                .setPositiveButton(Texts.shared.getText("ok"), null)
                .create()
                .show();
    }
}
