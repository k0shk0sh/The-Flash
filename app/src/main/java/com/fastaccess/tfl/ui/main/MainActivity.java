package com.fastaccess.tfl.ui.main;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.fastaccess.tfl.R;
import com.fastaccess.tfl.apps.AppsAdapter;
import com.fastaccess.tfl.apps.AppsModel;
import com.fastaccess.tfl.core.BaseActivity;
import com.fastaccess.tfl.helper.AnimUtil;
import com.fastaccess.tfl.helper.AppHelper;
import com.fastaccess.tfl.helper.GestureHelper;
import com.fastaccess.tfl.helper.Logger;
import com.fastaccess.tfl.helper.ViewHelper;
import com.fastaccess.tfl.ui.main.dock.ChooseAppPopupPager;
import com.fastaccess.tfl.ui.main.dock.MainDockModel;
import com.fastaccess.tfl.ui.main.drawer.MainDrawerModel;
import com.fastaccess.tfl.ui.main.drawer.MainDrawerPresenter;
import com.fastaccess.tfl.ui.wallpaper.WallpaperChangedReceiver;
import com.fastaccess.tfl.ui.wallpaper.WallpaperPickerActivity;
import com.fastaccess.tfl.ui.widget.DynamicRecyclerView;
import com.fastaccess.tfl.ui.widget.FontEditTextView;
import com.fastaccess.tfl.ui.widget.ForegroundImageView;
import com.fastaccess.tfl.ui.widget.drag.DragLayer;
import com.fastaccess.tfl.ui.widget.drag.DropSpot;
import com.mikhaellopez.circularfillableloaders.CircularFillableLoaders;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import butterknife.OnTouch;
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
    @Bind(R.id.openDrawer) View openDrawer;
    @Bind(R.id.recyclerView) DynamicRecyclerView recyclerView;
    @Bind(R.id.appsDrawer) View appsDrawer;
    @Bind(R.id.footer) View footer;
    @Bind(R.id.progress) CircularFillableLoaders progress;
    @Bind(R.id.dropSpot) DropSpot dropSpot;
    @Bind(R.id.mainLayout) DragLayer mainLayout;
    @Bind(R.id.dropLayout) View dropZone;
    @Bind(R.id.appInfo) DropSpot appInfo;
    @Bind(R.id.uninstallApp) DropSpot uninstallApp;
    private AppsAdapter adapter;
    private MainDrawerPresenter presenter;
    private final static String POPUP = "popup";
    private GestureDetector gestureDetector;

    @OnTouch(R.id.mainLayout) public boolean onGesture(MotionEvent e) {
        return gestureDetector.onTouchEvent(e);
    }

    @OnClick(R.id.openDrawer) void onOpenAppDrawer() {
        AnimUtil.circularRevealFromBottom(appsDrawer, openDrawer, appsDrawer.getVisibility() == View.INVISIBLE);
    }

    @OnClick(R.id.toggleMenu) void onMenu() {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    @OnClick(R.id.toggleSearch) void onToggle() {
        if (!appsDrawer.isShown())
            AnimUtil.circularRevealFromBottom(appsDrawer, true);
        AnimUtil.circularReveal(searchHolder, true);
        searchText.requestFocus();
        AppHelper.showKeyboard(searchText);
    }

    @OnClick(R.id.cancelSearch) void onCancel() {
        AnimUtil.circularReveal(searchHolder, false);
        searchText.setText("");
        adapter.getFilter().filter("");
        AppHelper.hideKeyboard(searchText);
    }

    @OnTextChanged(value = R.id.searchText, callback = OnTextChanged.Callback.TEXT_CHANGED)
    void onTextChanged(CharSequence txt, int a, int b, int c) {
        if (txt != null) {
            adapter.getFilter().filter(txt);
        }
    }

    @Override protected int layoutResId() {
        return R.layout.activity_main;
    }

    @Nullable @Override protected Toolbar toolbar() {
        return null;
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        MainDrawerPresenter presenter = getPresenter();
        drawerLayout.setStatusBarBackground(new ColorDrawable(Color.TRANSPARENT));
        navigationView.setNavigationItemSelectedListener(this);
        adapter = new AppsAdapter(presenter);
        recyclerView.setAdapter(adapter);
        gestureDetector = new GestureDetector(this, mainGesture);
        initWallpaper();
        mainLayout.setDragController(presenter.getDragController(), presenter);
        getPresenter().getDragController().addDropTarget(mainLayout);
        dropSpot.setup(mainLayout, presenter.getDragController());
        uninstallApp.setup(mainLayout, presenter.getDragController());
        appInfo.setup(mainLayout, presenter.getDragController());
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

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(MainActivity.this, "" + (resultCode == RESULT_OK), Toast.LENGTH_SHORT).show();
    }

    @Override public MainActivity getContext() {
        return this;
    }

    @Override public void onStartLoading() {
        recyclerView.showProgress(progress);
    }

    @Override public void onFinishedLoading(List<AppsModel> models) {
        recyclerView.hideProgress(progress);
        adapter.setModelList(models);
    }

    @Override public void onReset() {
        adapter.clear();
    }

    @Override public void onAppRemoved(int position) {
        adapter.remove(position);
    }

    @Override public void closeDrawer() {
        AnimUtil.circularRevealFromBottom(appsDrawer, false);
    }

    @Override public void onDropZone(boolean show) {
        ViewHelper.animateVisibility(!show, appBar);
        ViewHelper.animateVisibility(show, dropZone);
    }

    @Override public void onAppSelected(AppsModel model) {

    }

    private void initWallpaper() {
//        WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
//        Drawable wallpaperDrawable = wallpaperManager.getDrawable();
//        if (drawerLayout != null) drawerLayout.setBackground(wallpaperDrawable);
    }

    public DragLayer getMainLayout() {
        return mainLayout;
    }

    public MainDrawerPresenter getPresenter() {
        if (presenter == null) presenter = MainDrawerPresenter.with(this);
        return presenter;
    }

    public void onEvent(WallpaperChangedReceiver.WallpaperReceiver wallpaperReceiver) {
        initWallpaper();
    }

    private GestureHelper.SimpleGestureHelper mainGesture = new GestureHelper.SimpleGestureHelper() {
        @Override protected void onDoubleClick() {
            super.onDoubleClick();
            Logger.e("double");
            ChooseAppPopupPager popup = (ChooseAppPopupPager) getSupportFragmentManager().findFragmentByTag(POPUP);
            if (popup == null) {
                popup = new ChooseAppPopupPager();
            }
            popup.show(getSupportFragmentManager(), POPUP);

        }

        @Override protected void onLongClick() {
            super.onLongClick();
            Logger.e("Long");
        }

        @Override protected void onSwipeRight() {
            super.onSwipeRight();
            Logger.e("Right");
        }

        @Override protected void onSwipeLeft() {
            super.onSwipeLeft();
            Logger.e("Left");
        }

        @Override protected void onSwipeUp() {
            super.onSwipeUp();
            Logger.e("Up");
        }

        @Override protected void onSwipeDown() {
            super.onSwipeDown();
            Logger.e("Down");
        }
    };

}