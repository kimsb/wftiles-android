package kimstephenbovim.wordfeudtiles;

import android.content.DialogInterface;
import android.database.DataSetObserver;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class AlertDialogs {

    public static AlertDialog getLanguageDialog(final GameListActivity gameListActivity) {
        // Set the dialog title
        AlertDialog alertDialog = new AlertDialog.Builder(gameListActivity).setTitle(Texts.shared.getText("language"))
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
                .setSingleChoiceItems(Texts.shared.getLocalizedLanguages(), Texts.shared.getLocaleIndex(),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int clickIndex) {
                                System.out.println("KLIKKER PÅ " + clickIndex);
                                WFTiles.instance.setPreferredLocaleIndex(clickIndex);
                                gameListActivity.setupRecyclerView(WFTiles.instance.getGames());
                            }
                        })
                // Set the action buttons
                .setPositiveButton(Texts.shared.getText("ok"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK, so save the mSelectedItems results somewhere
                        // or return them to the component that opened the dialog
                    }
                })
                .create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                ListView listView = ((AlertDialog) dialogInterface).getListView();
                final ListAdapter originalAdapter = listView.getAdapter();

                listView.setAdapter(new ListAdapter() {
                    @Override
                    public int getCount() {
                        return originalAdapter.getCount();
                    }

                    @Override
                    public Object getItem(int id) {
                        return originalAdapter.getItem(id);
                    }

                    @Override
                    public long getItemId(int id) {
                        return originalAdapter.getItemId(id);
                    }

                    @Override
                    public int getItemViewType(int id) {
                        return originalAdapter.getItemViewType(id);
                    }

                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View view = originalAdapter.getView(position, convertView, parent);
                        TextView textView = (TextView) view;
                        textView.setTextSize(16);
                        textView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,
                                Math.round(WFTiles.instance.getResources().getDimension(R.dimen.alert_dialog_item_height))));
                        return view;
                    }

                    @Override
                    public int getViewTypeCount() {
                        return originalAdapter.getViewTypeCount();
                    }

                    @Override
                    public boolean hasStableIds() {
                        return originalAdapter.hasStableIds();
                    }

                    @Override
                    public boolean isEmpty() {
                        return originalAdapter.isEmpty();
                    }

                    @Override
                    public void registerDataSetObserver(DataSetObserver observer) {
                        originalAdapter.registerDataSetObserver(observer);

                    }

                    @Override
                    public void unregisterDataSetObserver(DataSetObserver observer) {
                        originalAdapter.unregisterDataSetObserver(observer);

                    }

                    @Override
                    public boolean areAllItemsEnabled() {
                        return originalAdapter.areAllItemsEnabled();
                    }

                    @Override
                    public boolean isEnabled(int position) {
                        return originalAdapter.isEnabled(position);
                    }

                });
            }
        });
        return alertDialog;
    }
}
