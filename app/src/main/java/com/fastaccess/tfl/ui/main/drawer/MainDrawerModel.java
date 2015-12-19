package com.fastaccess.tfl.ui.main.drawer;

import com.fastaccess.tfl.apps.AppsModel;
import com.fastaccess.tfl.ui.main.MainActivity;

import java.util.List;

/**
 * Created by Kosh on 19/12/15 9:30 AM
 */
public interface MainDrawerModel {

    MainActivity getContext();

    void onStartLoading();

    void onFinishedLoading(List<AppsModel> models);

    void onReset();

    void onAppRemoved(int position);

    void closeDrawer();
}
