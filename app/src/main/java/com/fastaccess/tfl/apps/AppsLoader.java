package com.fastaccess.tfl.apps;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import com.fastaccess.tfl.AppController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AppsLoader extends AsyncTaskLoader<List<AppsModel>> {
    private ApplicationsReceiver mAppsObserver;
    private final PackageManager mPm;
    private List<AppsModel> mApps;
    private IconCache mIconCache;

    public AppsLoader(Context ctx) {
        super(ctx);
        mPm = getContext().getPackageManager();
        mIconCache = AppController.getApp().getIconCache();
    }

    @Override public List<AppsModel> loadInBackground() {
        try {
            List<AppsModel> entries = new ArrayList<AppsModel>();
            Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
            mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            List<ResolveInfo> list = mPm.queryIntentActivities(mainIntent, 0);
            if (list == null) {
                list = new ArrayList<>();
            }
            Collections.sort(list, new ResolveInfo.DisplayNameComparator(mPm));
            for (ResolveInfo resolveInfo : list) {
                if (!resolveInfo.activityInfo.applicationInfo.packageName.equals(getContext().getPackageName())) {
                    AppsModel exists = AppsModel.getAppByPackage(resolveInfo.activityInfo.applicationInfo.packageName);
                    AppsModel model = new AppsModel(mPm, resolveInfo, mIconCache, null);
                    if (exists == null) {
                        model.save();
                        entries.add(model);
                    } else {
                        if (!exists.isHidden()) {
                            entries.add(model);
                        }
                    }
                }
            }
            return entries;
        } catch (Exception e) {//transaction is too large????.
            e.printStackTrace();
        }
        return getInstalledPackages(getContext(), mIconCache);
    }

    @Override public void deliverResult(List<AppsModel> apps) {
        if (isReset()) {
            if (apps != null) {
                return;
            }
        }
        List<AppsModel> oldApps = mApps;
        mApps = apps;
        if (isStarted()) {
            super.deliverResult(apps);
        }
    }

    @Override protected void onStartLoading() {
        if (mApps != null) {
            deliverResult(mApps);
        }
        if (mAppsObserver == null) {
            mAppsObserver = new ApplicationsReceiver(this);
        }
        if (takeContentChanged()) {
            mIconCache = AppController.getApp().getIconCache();
            forceLoad();
        } else if (mApps == null) {
            mIconCache = AppController.getApp().getIconCache();
            forceLoad();
        }


    }

    @Override protected void onStopLoading() {
        cancelLoad();
    }

    @Override protected void onReset() {
        onStopLoading();
        if (mApps != null) {
            mApps = null;
        }
        if (mAppsObserver != null) {
            getContext().unregisterReceiver(mAppsObserver);
            mAppsObserver = null;
        }
    }

    @Override public void onCanceled(List<AppsModel> apps) {
        super.onCanceled(apps);
    }

    @Override public void forceLoad() {
        super.forceLoad();
    }

    public static List<AppsModel> getInstalledPackages(Context context, IconCache iconCache) {
        final PackageManager pm = context.getPackageManager();
        Process process;
        List<AppsModel> result = new ArrayList<>();
        BufferedReader bufferedReader = null;
        try {
            process = Runtime.getRuntime().exec("pm list packages");
            bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                final String packageName = line.substring(line.indexOf(':') + 1);
                PackageInfo packageInfo = pm.getPackageInfo(packageName, 0);
                Intent mainIntent = pm.getLaunchIntentForPackage(packageInfo.applicationInfo.packageName);
                if (mainIntent != null) {
                    ResolveInfo resolveInfo = pm.resolveActivity(mainIntent, 0);
                    if (resolveInfo != null) {
                        if (!packageName.equalsIgnoreCase(context.getPackageName())) {
                            AppsModel check = AppsModel.getAppByPackage(resolveInfo.activityInfo.packageName);
                            AppsModel model = new AppsModel(pm, resolveInfo, iconCache, null);
                            if (check == null) {
                                model.save();
                                result.add(model);
                            } else {
                                if (!check.isHidden()) {
                                    result.add(model);
                                }
                            }
                        }
                    }
                }
            }
            process.waitFor();
            Collections.sort(result, AppsModel.sortApps);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null)
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return result;
    }
}
