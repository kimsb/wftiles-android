package kimstephenbovim.wordfeudtiles;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import kimstephenbovim.wordfeudtiles.domain.Game;
import kimstephenbovim.wordfeudtiles.event.GameLoadedEvent;
import kimstephenbovim.wordfeudtiles.event.LoginEvent;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static kimstephenbovim.wordfeudtiles.Constants.MESSAGE_GAME_ID;
import static kimstephenbovim.wordfeudtiles.Constants.MESSAGE_IS_TWOPANE;

/**
 * A fragment representing a single Game detail screen.
 * This fragment is either contained in a {@link GameListActivity}
 * in two-pane mode (on tablets) or a {@link GameDetailActivity}
 * on handsets.
 */
public class GameDetailFragment extends Fragment {

    private Game game;
    private long gameId;
    private boolean isCreated;
    private SwipeRefreshLayout swipeRefreshLayout;

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

        ProgressDialogHandler.shared.getGame(getActivity(), gameId, true);

        if (getArguments().containsKey(MESSAGE_IS_TWOPANE) && !getArguments().getBoolean(MESSAGE_IS_TWOPANE)) {
            swipeRefreshLayout = getActivity().findViewById(R.id.swipeRefreshDetail);
            swipeRefreshLayout.setOnRefreshListener(
                    new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            ProgressDialogHandler.shared.getGame(getActivity(), gameId, true);
                        }
                    }
            );
        }
    }

    private void draw() {
        LinearLayout linearLayout = getActivity().findViewById(R.id.game_detail_container);
        linearLayout.removeAllViews();

        View header = LayoutInflater.from(getActivity())
                .inflate(R.layout.game_detail_header, linearLayout);

        Glide.with(getActivity())
                .load(WFTiles.instance.getUser().getAvatarRoot() + "/80/" + game.getOpponent().getId())
                .apply(RequestOptions.circleCropTransform())
                .into((ImageView) header.findViewById(R.id.headerOpponentImageView));

        int diff = game.getPlayer().getScore() - game.getOpponent().getScore();
        String diffText = (diff > 0 ? "+" : "") + diff;

        TextView diffView = header.findViewById(R.id.headerDiffLabel);
        diffView.setText(diffText);

        if (diff > 0) {
            diffView.setBackgroundResource(R.drawable.green_diff);
        } else if (diff < 0) {
            diffView.setBackgroundResource(R.drawable.red_diff);
        } else {
            diffView.setBackgroundResource(R.drawable.yellow_diff);
        }

        TextView languageView = header.findViewById(R.id.headerLanguageTextView);
        languageView.setText(Texts.shared.getGameLanguage(game.getRuleset()));

        TextView scoreView = header.findViewById(R.id.headerScoreTextView);
        scoreView.setText(String.format("%d - %d", game.getPlayer().getScore(), game.getOpponent().getScore()));

        TextView lastMoveView = header.findViewById(R.id.headerLastMoveTextView);
        lastMoveView.setText(game.getLastMoveText());


        View yourTilesHeader = LayoutInflater.from(getActivity())
                .inflate(R.layout.game_detail_section_header, linearLayout, false);
        TextView yourTilesView = yourTilesHeader.findViewById(R.id.detailSectionHeaderTextView);
        yourTilesView.setText(Texts.shared.getText("yourTiles"));
        linearLayout.addView(yourTilesHeader);

        TileCanvas rack = new TileCanvas(getActivity(), game.getPlayer().getRack(), Constants.shared.getPoints(game.getRuleset()));
        linearLayout.addView(rack);

        View remainingTilesHeader = LayoutInflater.from(getActivity())
                .inflate(R.layout.game_detail_section_header, linearLayout, false);
        TextView remainingTilesView = remainingTilesHeader.findViewById(R.id.detailSectionHeaderTextView);

        if (game.getRemainingLetters() == null) {
            remainingTilesView.setText(Texts.shared.getText("remainingTiles"));
        } else {
            int inBag = Math.max(0, game.getRemainingLetters().size() - 7);
            remainingTilesView.setText(String.format(Texts.shared.getText("remainingTilesBag"), inBag));
        }
        linearLayout.addView(remainingTilesHeader);

        TileCanvas tiles = new TileCanvas(getActivity(), game.getRemainingLetters(), Constants.shared.getPoints(game.getRuleset()));
        linearLayout.addView(tiles);
    }

    void updateView() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                draw();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        if (game != null) {
            draw();
        }
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
            ProgressDialogHandler.shared.getGame(getActivity(), gameId, true);
        } else {
            isCreated = true;
        }
    }

    private void stopRefreshing() {
        getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onMessageEvent(GameLoadedEvent gameLoadedEvent) {
        ProgressDialogHandler.shared.cancel();
        stopRefreshing();

        if (gameLoadedEvent.getGame() == null) {
            alert(isOnline() ? "" : Texts.shared.getText("connectionError"));
            return;
        }
        game = gameLoadedEvent.getGame();
        updateView();
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onMessageEvent(LoginEvent loginEvent) {
        ProgressDialogHandler.shared.getGame(getActivity(), gameId, false);
    }

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    private void alert(final String errorMessage) {
        new AlertDialog.Builder(getActivity())
                .setTitle(Texts.shared.getText("loadingFailed"))
                .setMessage(errorMessage)
                .setPositiveButton(Texts.shared.getText("ok"), null)
                .create()
                .show();
    }
}
