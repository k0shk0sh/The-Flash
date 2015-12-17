package com.fastaccess.tfl.ui;

import android.app.LoaderManager;
import android.app.WallpaperManager;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.fastaccess.tfl.R;
import com.fastaccess.tfl.apps.AppsAdapter;
import com.fastaccess.tfl.apps.AppsLoader;
import com.fastaccess.tfl.apps.AppsModel;
import com.fastaccess.tfl.helper.AnimUtil;
import com.fastaccess.tfl.ui.wallpaper.WallpaperChangedReceiver;
import com.fastaccess.tfl.ui.wallpaper.WallpaperPickerActivity;
import com.fastaccess.tfl.ui.widget.DynamicRecyclerView;
import com.fastaccess.tfl.ui.widget.FontEditTextView;
import com.fastaccess.tfl.ui.widget.ForegroundImageView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

public class MainActivity extends AppCompatActivity implements OnNavigationItemSelectedListener,
        LoaderManager.LoaderCallbacks<List<AppsModel>>, AppsAdapter.OnAppClick {

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
    @Bind(R.id.openDrawer) FloatingActionButton openDrawer;
    @Bind(R.id.footer) View footer;
    private AppsAdapter adapter;

    @OnClick(R.id.openDrawer) void onOpenAppDrawer() {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        drawerLayout.setStatusBarBackground(new ColorDrawable(Color.TRANSPARENT));
        navigationView.setNavigationItemSelectedListener(this);
        adapter = new AppsAdapter(this);
        recyclerView.setAdapter(adapter);
        initWallpaper();
        getLoaderManager().initLoader(1, null, this);
    }

    @Override public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
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

    @Override public Loader<List<AppsModel>> onCreateLoader(int id, Bundle args) {
        return new AppsLoader(this);
    }

    @Override public void onLoadFinished(Loader<List<AppsModel>> loader, List<AppsModel> data) {
        adapter.setModelList(data);
    }

    @Override public void onLoaderReset(Loader<List<AppsModel>> loader) {
        adapter.clear();
    }

    @Override public void onAppClick(AppsModel model) {
        try {
            PackageManager manager = getPackageManager();
            Intent intent = manager.getLaunchIntentForPackage(model.getPackageName());
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            startActivity(intent);
            model.updateEntry(model.getPackageName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}