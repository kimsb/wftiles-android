package kimstephenbovim.wordfeudtiles;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import kimstephenbovim.wordfeudtiles.domain.Game;

/**
 * A fragment representing a single Game detail screen.
 * This fragment is either contained in a {@link GameListActivity}
 * in two-pane mode (on tablets) or a {@link GameDetailActivity}
 * on handsets.
 */
public class GameDetailFragment extends Fragment {

    /**
     * The dummy content this fragment is presenting.
     */
    private Game game;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public GameDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey("GAME_ID")) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.


            //TODO: finn ut av denne - nå er den bare kommentert ut
            //game = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                //appBarLayout.setTitle(game.getOpponent().getUsername());
                appBarLayout.setTitle(Long.toString(getArguments().getLong("GAME_ID")));
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.game_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (game != null) {
            ((TextView) rootView.findViewById(R.id.game_detail)).setText(game.getLastMove().getMainWord());
        }

        return rootView;
    }
}
