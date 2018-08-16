package kimstephenbovim.wordfeudtiles;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import kimstephenbovim.wordfeudtiles.domain.Game;
import kimstephenbovim.wordfeudtiles.rest.RestClient;

/**
 * An activity representing a list of Games. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link GameDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class GameListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean isTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        if (findViewById(R.id.game_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            isTwoPane = true;
        }

        //View recyclerView = findViewById(R.id.game_list);
        //assert recyclerView != null;
        //setupRecyclerView((RecyclerView) recyclerView);

        //TEST Rest-kall
        RestClient.login(this);
    }



    public void setupRecyclerView(List<Game> gameList) {//@NonNull RecyclerView recyclerView) {
        RecyclerView recyclerView = findViewById(R.id.game_list);
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, gameList, isTwoPane));
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final GameListActivity parentActivity;
        private final List<Game> gameList;
        private final boolean isTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Game game = (Game) view.getTag();
                if (isTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putLong("GAME_ID", game.getId());
                    GameDetailFragment fragment = new GameDetailFragment();
                    fragment.setArguments(arguments);
                    parentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.game_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, GameDetailActivity.class);
                    intent.putExtra("GAME_ID", game.getId());

                    context.startActivity(intent);
                }
            }
        };

        SimpleItemRecyclerViewAdapter(GameListActivity parent,
                                      List<Game> games,
                                      boolean twoPane) {
            gameList = games;
            parentActivity = parent;
            isTwoPane = twoPane;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.game_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            //holder.opponentText.setText(gameList.get(position).getOpponent().getUsername());
            holder.opponentText.setText("motspiller");
            holder.languageText.setText("norsk");
            holder.scoreText.setText(Long.toString(gameList.get(position).getId()));
            holder.lastMoveText.setText("lastMove");

            holder.itemView.setTag(gameList.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return gameList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView opponentText;
            final TextView languageText;
            final TextView scoreText;
            final TextView lastMoveText;

            ViewHolder(View view) {
                super(view);
                opponentText = view.findViewById(R.id.opponentText);
                languageText = view.findViewById(R.id.languageText);
                scoreText = view.findViewById(R.id.scoreText);
                lastMoveText = view.findViewById(R.id.lastMoveText);
            }
        }
    }
}
