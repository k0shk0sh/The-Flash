package com.fastaccess.tfl.ui.main.drawer;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.fastaccess.tfl.apps.AppsAdapter;
import com.fastaccess.tfl.apps.AppsLoader;
import com.fastaccess.tfl.apps.AppsModel;
import com.fastaccess.tfl.helper.Logger;
import com.fastaccess.tfl.ui.widget.drag.DragController;
import com.fastaccess.tfl.ui.widget.drag.DropSpot;

import java.util.List;

/**
 * Created by Kosh on 19/12/15 9:32 AM
 */
public class MainDrawerPresenter implements LoaderManager.LoaderCallbacks<List<AppsModel>>, AppsAdapter.OnAppClick,
        DropSpot.OnDragLisenter {

    private final MainDrawerModel mainDrawerModel;
    private final DragController dragController;

    private MainDrawerPresenter(MainDrawerModel mainDrawerModel) {
        this.mainDrawerModel = mainDrawerModel;
        mainDrawerModel.getContext().getLoaderManager().initLoader(1, null, this);
        dragController = new DragController(mainDrawerModel.getContext());
    }

    public static MainDrawerPresenter with(MainDrawerModel mainDrawerModel) {
        return new MainDrawerPresenter(mainDrawerModel);
    }

    @Override public Loader<List<AppsModel>> onCreateLoader(int id, Bundle args) {
        mainDrawerModel.onStartLoading();
        return new AppsLoader(mainDrawerModel.getContext());
    }

    @Override public void onLoadFinished(Loader<List<AppsModel>> loader, List<AppsModel> data) {
        mainDrawerModel.onFinishedLoading(data);
    }

    @Override public void onLoaderReset(Loader<List<AppsModel>> loader) {
        mainDrawerModel.onReset();
    }

    @Override public void onAppClick(AppsModel model) {
        try {
            PackageManager manager = mainDrawerModel.getContext().getPackageManager();
            Intent intent = manager.getLaunchIntentForPackage(model.getPackageName());
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            mainDrawerModel.getContext().startActivity(intent);
            model.updateEntry(model.getPackageName());
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(mainDrawerModel.getContext(), "App Not Found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override public void onLongClick(AppsModel model, int position, View v) {
        dragController.startDrag(v, mainDrawerModel.getContext().getMainLayout(), v, DragController.DRAG_ACTION_MOVE);

//        dragController.startDrag(v, mDragLayer, model, DragController.DRAG_ACTION_MOVE);
//        PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
//        popupMenu.inflate(R.menu.cab_delete_wallpapers);
//        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//            @Override public boolean onMenuItemClick(MenuItem item) {
//                if (item.getItemId() == R.id.menu_delete) {
//                    return true;
//                }
//                return false;
//            }
//        });
//        popupMenu.show();
    }

    public DragController getDragController() {
        return dragController;
    }

    @Override public void onDrop(View v, View where) {
        Logger.e(v.getClass().getSimpleName() + "||" + where.getClass().getSimpleName());
    }

    @Override public void onStart() {
        mainDrawerModel.closeDrawer();
    }
}
