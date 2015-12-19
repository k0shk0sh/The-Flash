package com.fastaccess.tfl.ui.main.dock;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Kosh on 19/12/15 10:45 AM
 */
public class ChoosePagerAdapter extends FragmentStatePagerAdapter {

    private Context context;

    public ChoosePagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    @Override public Fragment getItem(int position) {
        return position == 0 ? ChooseAppFragment.getInstance(false) : ChooseAppFragment.getInstance(true);
    }

    @Override public int getCount() {
        return 2;
    }

    @Override public CharSequence getPageTitle(int position) {
        return position == 0 ? "App" : "Folder";
    }
}
