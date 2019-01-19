package kimstephenbovim.wordfeudtiles;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
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
import static kimstephenbovim.wordfeudtiles.Constants.MESSAGE_SKIP_LOGIN;
import static kimstephenbovim.wordfeudtiles.event.LoginResult.OK;

public class GameDetailFragment extends Fragment {

    private Game game;
    public Long gameId;
    private boolean isCreated;

    public GameDetailFragment() {
    }

    public String getOpponentUsername() {
        return game == null || game.getOpponent() == null
                ? ""
                : game.getOpponent().presentableUsername();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gameId = getArguments().getLong(MESSAGE_GAME_ID);
        game = WFTiles.instance.getGame(gameId);

        ProgressDialogHandler.shared.getGame(getActivity(), gameId, true);
        FragmentActivity activity = getActivity();
        if (activity instanceof GameDetailActivity) {
            GameDetailActivity gameDetailActivity = (GameDetailActivity) activity;
            gameDetailActivity.gameDetailFragment = this;
        } else if (activity instanceof GameListActivity) {
            GameListActivity gameListActivity = (GameListActivity) activity;
            gameListActivity.gameDetailFragment = this;
        }
    }

    private void draw() {
        LinearLayout linearLayout = getActivity().findViewById(R.id.game_detail_container);

        if (linearLayout == null) {
            return;
        }

        linearLayout.removeAllViews();
        if (gameId == null) {
            return;
        }

        View header = LayoutInflater.from(getActivity())
                .inflate(R.layout.game_detail_header, linearLayout);

        Glide.with(getActivity())
                .load(WFTiles.instance.getLoggedInUser().getAvatarRoot() + "/80/" + game.getOpponent().getId())
                .apply(RequestOptions.circleCropTransform())
                .apply(RequestOptions.errorOf(R.drawable.circle))
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

        if (WFTiles.instance.getPreferences().isViewOverview()) {
            linearLayout.addView(new TileOverviewCanvas(getActivity(), game.getRemainingLetters(), game.getRuleset()));
        } else {
            linearLayout.addView(new TileCanvas(getActivity(), game.getRemainingLetters(), Constants.shared.getPoints(game.getRuleset())));
        }
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
        final FragmentActivity activity = getActivity();
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (activity instanceof GameDetailActivity) {
                    GameDetailActivity gameDetailActivity = (GameDetailActivity) activity;
                    if (gameDetailActivity.swipeRefreshLayout != null) {
                        gameDetailActivity.swipeRefreshLayout.setRefreshing(false);
                    }
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
        if (loginEvent.getLoginResult() == OK) {
            if (gameId != null && WFTiles.instance.getGame(gameId) != null) {
                ProgressDialogHandler.shared.getGame(getActivity(), gameId, false);
            }
        } else {
            System.out.println("Login feiler");
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.putExtra(MESSAGE_SKIP_LOGIN, false);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            getActivity().finish();
        }
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
