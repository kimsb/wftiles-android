package kimstephenbovim.wordfeudtiles;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import kimstephenbovim.wordfeudtiles.domain.Game;
import kimstephenbovim.wordfeudtiles.domain.GameRow;
import kimstephenbovim.wordfeudtiles.event.GamesLoadedEvent;
import kimstephenbovim.wordfeudtiles.event.LoginEvent;
import kimstephenbovim.wordfeudtiles.rest.RestClient;

import static kimstephenbovim.wordfeudtiles.Constants.MESSAGE_GAME_ID;
import static kimstephenbovim.wordfeudtiles.Constants.MESSAGE_OPPONENT_NAME;
import static kimstephenbovim.wordfeudtiles.Constants.MESSAGE_SKIP_LOGIN;
import static kimstephenbovim.wordfeudtiles.domain.GameRowType.HEADER;
import static kimstephenbovim.wordfeudtiles.event.LoginResult.OK;

public class GameListActivity extends AppCompatActivity {

    private boolean isTwoPane;
    private List<Game> games;
    private boolean isCreated;
    private String appbarTitleSpacing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_list);

        Toolbar toolbar = findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            if (WFTiles.instance.getUser() != null) {
                actionBar.setTitle(WFTiles.instance.getUser().presentableFullUsername());
            }
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (findViewById(R.id.game_detail_container) != null) {
            isTwoPane = true;
            setAppbarTitleSpacing();
        }

        setupRecyclerView(WFTiles.instance.getGames());

        RestClient.getGames(true);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra(MESSAGE_SKIP_LOGIN, false);
            NavUtils.navigateUpTo(this, intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setupRecyclerView(List<Game> games) {
        this.games = games;
        RecyclerView recyclerView = findViewById(R.id.game_list);
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, Mapper.mapGamesToGameRows(games), isTwoPane));
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
                RestClient.getGame(game.getId(), true);

                if (isTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putLong(MESSAGE_GAME_ID, game.getId());
                    arguments.putString(MESSAGE_OPPONENT_NAME, game.getOpponent().presentableUsername());
                    GameDetailFragment fragment = new GameDetailFragment();
                    fragment.setArguments(arguments);
                    parentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.game_detail_container, fragment, "her_er_min_tag")
                            .commit();

                    parentActivity.getSupportActionBar().setTitle(WFTiles.instance.getUser().presentableFullUsername()
                            + parentActivity.appbarTitleSpacing
                            + game.getOpponent().presentableUsername());
                    RestClient.getGames(true);

                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, GameDetailActivity.class);
                    intent.putExtra(MESSAGE_GAME_ID, game.getId());
                    intent.putExtra(MESSAGE_OPPONENT_NAME, game.getOpponent().presentableUsername());

                    context.startActivity(intent);
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
            RestClient.getGames(true);
        } else {
            isCreated = true;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(GamesLoadedEvent gamesLoadedEvent) {
        if (gamesLoadedEvent.getGames() == null) {
            alert(isOnline() ? "" : Texts.shared.getText("connectionError"));
            return;
        }

        //TODO refactor game-comparings + all compare-logic outside main thread
        List<Game> loadedGames = gamesLoadedEvent.getGames();
        System.out.println("In Activity, recieved Event for " + loadedGames.size() + " games: ");
        //if any new -> update view
        if (loadedGames.size() != games.size()) {
            setupRecyclerView(loadedGames);
        } else {
            for (Game storedGame : games) {
                for (Game loadedGame : loadedGames) {
                    if (storedGame.getId() == loadedGame.getId() &&
                            storedGame.getUpdated() != loadedGame.getUpdated()) {
                        setupRecyclerView(loadedGames);
                        return;
                    }
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onMessageEvent(LoginEvent loginEvent) {
        if (loginEvent.getLoginResult() == OK) {
            RestClient.getGames(false);
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
