package com.fastaccess.tfl.ui.main;

import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.fastaccess.tfl.R;
import com.fastaccess.tfl.apps.AppsAdapter;
import com.fastaccess.tfl.apps.AppsModel;
import com.fastaccess.tfl.core.BaseActivity;
import com.fastaccess.tfl.helper.AnimUtil;
import com.fastaccess.tfl.ui.main.dock.ChooseAppPopupPager;
import com.fastaccess.tfl.ui.main.dock.MainDockModel;
import com.fastaccess.tfl.ui.main.drawer.MainDrawerModel;
import com.fastaccess.tfl.ui.main.drawer.MainDrawerPresenter;
import com.fastaccess.tfl.ui.wallpaper.WallpaperChangedReceiver;
import com.fastaccess.tfl.ui.wallpaper.WallpaperPickerActivity;
import com.fastaccess.tfl.ui.widget.DynamicRecyclerView;
import com.fastaccess.tfl.ui.widget.FontEditTextView;
import com.fastaccess.tfl.ui.widget.ForegroundImageView;
import com.mikhaellopez.circularfillableloaders.CircularFillableLoaders;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

public class MainActivity extends BaseActivity implements OnNavigationItemSelectedListener, MainDrawerModel, MainDockModel {

    @Bind(R.id.toggleSearch) ForegroundImageView toggleSearch;
    @Bind(R.id.titleHolder) View titleHolder;
    @Bind(R.id.searchText) FontEditTextView searchText;
    @Bind(R.id.cancelSearch) ForegroundImageView cancelSearch;
    @Bind(R.id.appBarCard) CardView appBarCard;
    @Bind(R.id.appBar) AppBarLayout appBar;
    @Bind(R.id.drawerLayout) DrawerLayout drawerLayout;
    @Bind(R.id.searchHolder) View searchHolder;
    @Bind(R.id.navigationView) NavigationView navigationView;
    @Bind(R.id.toggleMenu) ForegroundImageView toggleMenu;
    @Bind(R.id.recyclerView) DynamicRecyclerView recyclerView;
    @Bind(R.id.appsDrawer) View appsDrawer;
    @Bind(R.id.footer) View footer;
    @Bind(R.id.progress) CircularFillableLoaders progress;
    private AppsAdapter adapter;
    private MainDrawerPresenter presenter;
    private final static String POPUP = "popup";

    @OnClick(R.id.openDrawer) void onOpenAppDrawer() {
        ChooseAppPopupPager popup = (ChooseAppPopupPager) getSupportFragmentManager().findFragmentByTag(POPUP);
        if (popup == null) {
            popup = new ChooseAppPopupPager();
        }
        popup.show(getSupportFragmentManager(), POPUP);
        AnimUtil.circularRevealFromBottom(appsDrawer, appsDrawer.getVisibility() == View.INVISIBLE);
    }

    @OnClick(R.id.toggleMenu) void onMenu() {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    @OnClick(R.id.toggleSearch) void onToggle() {
        AnimUtil.circularReveal(searchHolder, true);
    }

    @OnClick(R.id.cancelSearch) void onCancel() {
        AnimUtil.circularReveal(searchHolder, false);
    }

    @Override protected int layoutResId() {
        return R.layout.activity_main;
    }

    @Nullable @Override protected Toolbar toolbar() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        drawerLayout.setStatusBarBackground(new ColorDrawable(Color.TRANSPARENT));
        navigationView.setNavigationItemSelectedListener(this);
        adapter = new AppsAdapter(getPresenter());
        recyclerView.setAdapter(adapter);
        initWallpaper();
    }

    @Override public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        if (appsDrawer.isShown()) {
            AnimUtil.circularRevealFromBottom(appsDrawer, false);
        }
    }

    @Override protected void onDestroy() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }

    @Override public boolean onNavigationItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.wallpaper) {
            startActivity(new Intent(this, WallpaperPickerActivity.class));
            return true;
        }
        return false;
    }

    private void initWallpaper() {
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
        Drawable wallpaperDrawable = wallpaperManager.getDrawable();
        if (drawerLayout != null) drawerLayout.setBackground(wallpaperDrawable);
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(MainActivity.this, "" + (resultCode == RESULT_OK), Toast.LENGTH_SHORT).show();
    }

    public void onEvent(WallpaperChangedReceiver.WallpaperReceiver wallpaperReceiver) {
        initWallpaper();
    }

    @Override public MainActivity getContext() {
        return this;
    }

    @Override public void onStartLoading() {
        progress.setVisibility(View.VISIBLE);
    }

    @Override public void onFinishedLoading(List<AppsModel> models) {
        progress.setVisibility(View.GONE);
        adapter.setModelList(models);
    }

    @Override public void onReset() {
        adapter.clear();
    }

    @Override public void onAppRemoved(int position) {
        adapter.remove(position);
    }

    public MainDrawerPresenter getPresenter() {
        if (presenter == null) presenter = MainDrawerPresenter.with(this);
        return presenter;
    }

    @Override public void onAppSelected(AppsModel model) {

    }
}