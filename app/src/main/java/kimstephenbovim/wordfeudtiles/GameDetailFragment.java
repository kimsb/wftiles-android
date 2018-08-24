package kimstephenbovim.wordfeudtiles;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import kimstephenbovim.wordfeudtiles.domain.Game;
import kimstephenbovim.wordfeudtiles.event.GameLoadedEvent;
import kimstephenbovim.wordfeudtiles.event.LoginEvent;
import kimstephenbovim.wordfeudtiles.rest.RestClient;

import static kimstephenbovim.wordfeudtiles.Constants.MESSAGE_GAME_ID;

/**
 * A fragment representing a single Game detail screen.
 * This fragment is either contained in a {@link GameListActivity}
 * in two-pane mode (on tablets) or a {@link GameDetailActivity}
 * on handsets.
 */
public class GameDetailFragment extends Fragment {

    private Game game;
    private long gameId;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public GameDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gameId = getArguments().getLong(MESSAGE_GAME_ID);
        game = WFTiles.instance.getGame(gameId);

        if (getArguments().containsKey(MESSAGE_GAME_ID)) {
            RestClient.getGame(gameId);

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(Long.toString(gameId));
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.game_detail, container, false);

        // 1. get a reference to recyclerView
        //RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.game_detail_container);
        RecyclerView recyclerView = (RecyclerView) rootView;

        // 2. set layoutManger
        //recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 8);
        recyclerView.setLayoutManager(gridLayoutManager);

        // this is data fro recycler view


        // 3. create an adapter
        TileRecyclerViewAdapter tileRecyclerViewAdapter = new TileRecyclerViewAdapter(game);
        // 4. set adapter
        recyclerView.setAdapter(tileRecyclerViewAdapter);
        // 5. set item animator to DefaultAnimator
        //trens denne?
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //return rootView;
        return recyclerView;
    }

    public static class TileRecyclerViewAdapter
            extends RecyclerView.Adapter<GameDetailFragment.TileRecyclerViewAdapter.ViewHolder> {

        //private final GameListActivity parentActivity;
        private final List<String> remainingLetters;
        private final Game game;

        TileRecyclerViewAdapter(Game game) {
            this.remainingLetters = game.getRemainingLetters();
            this.game = game;
            //parentActivity = parent;
        }

        @Override
        public GameDetailFragment.TileRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.tile_view, parent, false);
            return new GameDetailFragment.TileRecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final GameDetailFragment.TileRecyclerViewAdapter.ViewHolder holder, int position) {
            String letter = remainingLetters.get(position);

            holder.letterText.setText(letter);
            int score = Constants.shared.getPoints(game.getRuleset()).get(letter);
            holder.scoreText.setText(score > 0 ? String.valueOf(score) : "");

            //usikker på denne? vil jo bli lik for mange
            holder.itemView.setTag(remainingLetters.get(position));
        }

        @Override
        public int getItemCount() {
            return remainingLetters.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView letterText;
            final TextView scoreText;

            ViewHolder(View view) {
                super(view);
                letterText = view.findViewById(R.id.letter);
                scoreText = view.findViewById(R.id.score);
            }
        }
    }

    private void updateView() {
        // Reload current fragment
        Fragment frg = null;
        frg = getActivity().getSupportFragmentManager().findFragmentByTag("her_er_min_tag");
        final FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.detach(frg);
        ft.attach(frg);
        ft.commit();
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
    public void onMessageEvent(GameLoadedEvent gameLoadedEvent) {
        System.out.println("I Fragment, updater: " + game.getLetterCount().toString());
        updateView();
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onMessageEvent(LoginEvent loginEvent) {
        RestClient.getGame(gameId);
    }
}
