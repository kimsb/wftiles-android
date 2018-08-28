package kimstephenbovim.wordfeudtiles;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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
import kimstephenbovim.wordfeudtiles.domain.GameDetailItem;
import kimstephenbovim.wordfeudtiles.event.GameLoadedEvent;
import kimstephenbovim.wordfeudtiles.event.LoginEvent;
import kimstephenbovim.wordfeudtiles.rest.RestClient;
import kimstephenbovim.wordfeudtiles.util.ListSpacingDecoration;

import static kimstephenbovim.wordfeudtiles.Constants.MESSAGE_GAME_ID;
import static kimstephenbovim.wordfeudtiles.Mapper.mapStringsToGameDetailItems;
import static kimstephenbovim.wordfeudtiles.domain.GameDetailType.HEADER;
import static kimstephenbovim.wordfeudtiles.domain.GameDetailType.SECTIONHEADER;
import static kimstephenbovim.wordfeudtiles.domain.GameDetailType.TILE;

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

        int spanCount = 8; // 3 columns
        int spacing = 10; // 50px
        boolean includeEdge = false;
        recyclerView.addItemDecoration(new ListSpacingDecoration(spacing));

        // 3. create an adapter
        final GameDetailItemRecyclerViewAdapter gameDetailItemRecyclerViewAdapter = new GameDetailItemRecyclerViewAdapter(getActivity(), game);

        GridLayoutManager.SpanSizeLookup spanSizeLookup = new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                //this must return max spansize
                if (gameDetailItemRecyclerViewAdapter.getItemViewType(position) == TILE.ordinal()) {
                    return 1;
                } else {
                    return 8;
                }
            }
        };
        spanSizeLookup.setSpanIndexCacheEnabled(true);
        // for setting the right span for headers
        gridLayoutManager.setSpanSizeLookup(spanSizeLookup);

        // 4. set adapter
        recyclerView.setAdapter(gameDetailItemRecyclerViewAdapter);
        // 5. set item animator to DefaultAnimator
        //trens denne?
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //return rootView;
        return recyclerView;
    }

    public static class GameDetailItemRecyclerViewAdapter
            extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private final Activity parentActivity;
        private final List<GameDetailItem> gameDetailItems;
        private final Game game;

        GameDetailItemRecyclerViewAdapter(Activity parentActivity, Game game) {
            this.parentActivity = parentActivity;
            //TODO refactor, this is ugly
            List<GameDetailItem> rackTiles = mapStringsToGameDetailItems(game.getPlayer().getRack());
            rackTiles.add(0, new GameDetailItem(SECTIONHEADER, "YOUR TILES"));
            rackTiles.add(0, new GameDetailItem(HEADER, "BOGUS"));
            List<GameDetailItem> letterTiles = mapStringsToGameDetailItems(game.getRemainingLetters());
            letterTiles.add(0, new GameDetailItem(SECTIONHEADER, "REMAINING TILES"));
            rackTiles.addAll(letterTiles);
            gameDetailItems = rackTiles;

            this.game = game;
            //parentActivity = parent;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == HEADER.ordinal()) {
                return new HeaderViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.game_detail_header, parent, false));
            }
            else if (viewType == SECTIONHEADER.ordinal()) {
                return new SectionHeaderViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.game_detail_section_header, parent, false));
            } else {
                return new TileViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.tile_view, parent, false));
            }
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
            GameDetailItem gameDetailItem = gameDetailItems.get(position);
            if (gameDetailItem.getGameDetailType().equals(HEADER)) {
                HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;

                Glide.with(parentActivity)
                        .load(WFTiles.instance.getUser().getAvatarRoot() + "/80/" + game.getOpponent().getId())
                        .apply(RequestOptions.circleCropTransform())
                        .into(headerViewHolder.opponentImage);
                headerViewHolder.languageText.setText(Texts.shared.getGameLanguage(game.getRuleset()));
                headerViewHolder.scoreText.setText(String.format("%d - %d", game.getPlayer().getScore(), game.getOpponent().getScore()));
                headerViewHolder.lastMoveText.setText(game.getLastMoveText());
            }
            else if (gameDetailItem.getGameDetailType().equals(SECTIONHEADER)) {
                SectionHeaderViewHolder sectionHeaderViewHolder = (SectionHeaderViewHolder) holder;
                sectionHeaderViewHolder.sectionHeaderText.setText(gameDetailItem.getItemText());
            } else {
                TileViewHolder tileViewHolder = (TileViewHolder) holder;
                tileViewHolder.letterText.setText(gameDetailItem.getItemText());
                int score = Constants.shared.getPoints(game.getRuleset()).get(gameDetailItem.getItemText());
                tileViewHolder.scoreText.setText(score > 0 ? String.valueOf(score) : "");
            }
            //usikker på denne? vil jo bli lik for mange
            holder.itemView.setTag(gameDetailItems.get(position));
        }

        @Override
        public int getItemViewType(int position) {
            return gameDetailItems.get(position).getGameDetailType().ordinal();
        }

        @Override
        public int getItemCount() {
            return gameDetailItems.size();
        }

        class TileViewHolder extends RecyclerView.ViewHolder {
            final TextView letterText;
            final TextView scoreText;

            TileViewHolder(View view) {
                super(view);
                letterText = view.findViewById(R.id.letter);
                scoreText = view.findViewById(R.id.score);
            }
        }

        class HeaderViewHolder extends RecyclerView.ViewHolder {
            final ImageView opponentImage;
            final TextView languageText;
            final TextView scoreText;
            final TextView lastMoveText;

            HeaderViewHolder(View view) {
                super(view);
                opponentImage = view.findViewById(R.id.headerOpponentImageView);
                languageText = view.findViewById(R.id.headerLanguageTextView);
                scoreText = view.findViewById(R.id.headerScoreTextView);
                lastMoveText = view.findViewById(R.id.headerLastMoveTextView);
            }
        }

        class SectionHeaderViewHolder extends RecyclerView.ViewHolder {
            final TextView sectionHeaderText;

            SectionHeaderViewHolder(View view) {
                super(view);
                sectionHeaderText = view.findViewById(R.id.detailSectionHeaderTextView);
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

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onMessageEvent(GameLoadedEvent gameLoadedEvent) {
        System.out.println("I Fragment, updater: " + game.getLetterCount().toString());
        updateView();
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onMessageEvent(LoginEvent loginEvent) {
        RestClient.getGame(gameId);
    }
}
