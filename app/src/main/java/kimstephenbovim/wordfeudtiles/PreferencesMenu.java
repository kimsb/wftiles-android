package kimstephenbovim.wordfeudtiles;

import android.content.Context;
import android.support.v4.view.MenuCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

public class PreferencesMenu {

    public static Menu initMenu(Menu menu) {
        MenuCompat.setGroupDividerEnabled(menu, true);
        menu.getItem(0).setTitle(Texts.shared.getText("language"));
        menu.getItem(1).setTitle(Texts.shared.getText("standard"));
        menu.getItem(2).setTitle(Texts.shared.getText("overview"));
        menu.getItem(3).setTitle(Texts.shared.getText("alphabetical"));
        menu.getItem(4).setTitle(Texts.shared.getText("vowelsConsonants"));

        SubMenu languagesSubmenu = menu.getItem(0).getSubMenu();
        CharSequence[] localizedLanguages = Texts.shared.getLocalizedLanguages();
        for (int i = 0; i < localizedLanguages.length; i++) {
            languagesSubmenu.getItem(i).setTitle(localizedLanguages[i]);
        }

        languagesSubmenu.getItem(Texts.shared.getLocaleIndex()).setChecked(true);
        if (WFTiles.instance.getPreferences().isViewOverview()) {
            menu.getItem(2).setChecked(true);
        } else {
            menu.getItem(1).setChecked(true);
        }
        if (WFTiles.instance.getPreferences().isSortVowelsConsonants()) {
            menu.getItem(4).setChecked(true);
        } else {
            menu.getItem(3).setChecked(true);
        }

        return menu;
    }

    public static void onClicked(MenuItem menuItem, Context context) {
        switch (menuItem.getItemId()) {
            case R.id.language_0:
                WFTiles.instance.setPreferredLocaleIndex(0);
                break;
            case R.id.language_1:
                WFTiles.instance.setPreferredLocaleIndex(1);
                break;
            case R.id.language_2:
                WFTiles.instance.setPreferredLocaleIndex(2);
                break;
            case R.id.language_3:
                WFTiles.instance.setPreferredLocaleIndex(3);
                break;
            case R.id.language_4:
                WFTiles.instance.setPreferredLocaleIndex(4);
                break;
            case R.id.language_5:
                WFTiles.instance.setPreferredLocaleIndex(5);
                break;
            case R.id.language_6:
                WFTiles.instance.setPreferredLocaleIndex(6);
                break;
            case R.id.language_7:
                WFTiles.instance.setPreferredLocaleIndex(7);
                break;
            case R.id.language_8:
                WFTiles.instance.setPreferredLocaleIndex(8);
                break;
            case R.id.language_9:
                WFTiles.instance.setPreferredLocaleIndex(9);
                break;
            case R.id.view_standard:
                WFTiles.instance.setPreferredView(false);
                break;
            case R.id.view_overview:
                WFTiles.instance.setPreferredView(true);
                break;
            case R.id.sorting_alphabetical:
                WFTiles.instance.setPreferredSorting(false);
                break;
            case R.id.sorting_vowels_consonants:
                WFTiles.instance.setPreferredSorting(true);
                break;
            default:
                return;
        }
        menuItem.setChecked(true);
        //main part for holding onto the menu
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
        menuItem.setActionView(new View(context));
        menuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return false;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                return false;
            }
        });
    }
}
