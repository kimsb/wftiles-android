package kimstephenbovim.wordfeudtiles;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.List;

import kimstephenbovim.wordfeudtiles.domain.User;

import static kimstephenbovim.wordfeudtiles.Constants.MESSAGE_NO_LOGIN_SUGGESTION;
import static kimstephenbovim.wordfeudtiles.Constants.MESSAGE_SKIP_LOGIN;

class DrawerMenu {

    static void initNavigationView(GameListActivity gameListActivity, NavigationView navigationView) {
        if (navigationView != null) {

            navigationView.setItemIconTintList(null);

            View headerView = navigationView.getHeaderView(0);
            ImageView loggedInAvatar = headerView.findViewById(R.id.loggedInAvatar);
            TextView loggedInUsername = headerView.findViewById(R.id.loggedInUsername);

            loggedInUsername.setText(WFTiles.instance.getLoggedInUser().getUsername());
            Glide.with(gameListActivity)
                    .load(WFTiles.instance.getLoggedInUser().getAvatarRoot() + "/80/" + WFTiles.instance.getLoggedInUser().getId())
                    .apply(RequestOptions.circleCropTransform())
                    .apply(RequestOptions.errorOf(R.drawable.circle))
                    .into(loggedInAvatar);

            Menu menu = navigationView.getMenu();

            menu.findItem(R.id.logout).setTitle(Texts.shared.getText("logout"));

            List<User> users = WFTiles.instance.getUsers();
            for (int i = 1; i < 10; i++) {
                final MenuItem menuItem = menu.getItem(i);
                if (users.size() > i) {
                    User user = users.get(i);
                    Glide.with(gameListActivity)
                            .load(user.getAvatarRoot() + "/80/" + user.getId())
                            .apply(RequestOptions.circleCropTransform())
                            .apply(RequestOptions.errorOf(R.drawable.circle))
                            .into(new SimpleTarget<Drawable>() {
                                @Override
                                public void onResourceReady(@NonNull Drawable resource,
                                                            @Nullable Transition<? super Drawable> transition) {
                                    menuItem.setIcon(resource);
                                }
                            });
                    menuItem.setTitle(user.getUsername());
                    menuItem.setVisible(true);
                } else {
                    menuItem.setVisible(false);
                }
            }
        }
    }

    static boolean onNavigationItemSelected(GameListActivity gameListActivity, MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.logout:
                WFTiles.instance.logoutCurrentUser();
                User nextUser = WFTiles.instance.getLoggedInUser();
                if (nextUser != null) {
                    ProgressDialogHandler.shared.login(gameListActivity, nextUser);
                } else {
                    Intent intent = new Intent(gameListActivity, LoginActivity.class);
                    intent.putExtra(MESSAGE_SKIP_LOGIN, false);
                    gameListActivity.startActivity(intent);
                    gameListActivity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    gameListActivity.finish();
                    return true;
                }
                break;
            case R.id.add:
                Intent intent = new Intent(gameListActivity, LoginActivity.class);
                intent.putExtra(MESSAGE_SKIP_LOGIN, false);
                intent.putExtra(MESSAGE_NO_LOGIN_SUGGESTION, true);
                gameListActivity.startActivity(intent);
                gameListActivity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                gameListActivity.finish();
                return true;
            case R.id.user_1:
                ProgressDialogHandler.shared.login(gameListActivity, WFTiles.instance.getUsers().get(1));
                break;
            case R.id.user_2:
                ProgressDialogHandler.shared.login(gameListActivity, WFTiles.instance.getUsers().get(2));
                break;
            case R.id.user_3:
                ProgressDialogHandler.shared.login(gameListActivity, WFTiles.instance.getUsers().get(3));
                break;
            case R.id.user_4:
                ProgressDialogHandler.shared.login(gameListActivity, WFTiles.instance.getUsers().get(4));
                break;
            case R.id.user_5:
                ProgressDialogHandler.shared.login(gameListActivity, WFTiles.instance.getUsers().get(5));
                break;
            case R.id.user_6:
                ProgressDialogHandler.shared.login(gameListActivity, WFTiles.instance.getUsers().get(6));
                break;
            case R.id.user_7:
                ProgressDialogHandler.shared.login(gameListActivity, WFTiles.instance.getUsers().get(7));
                break;
            case R.id.user_8:
                ProgressDialogHandler.shared.login(gameListActivity, WFTiles.instance.getUsers().get(8));
                break;
            case R.id.user_9:
                ProgressDialogHandler.shared.login(gameListActivity, WFTiles.instance.getUsers().get(9));
                break;
        }
        DrawerLayout drawer = gameListActivity.findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
