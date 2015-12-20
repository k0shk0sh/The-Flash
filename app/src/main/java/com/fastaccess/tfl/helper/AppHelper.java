package com.fastaccess.tfl.helper;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by kosh20111 on 10/7/2015
 */
public class AppHelper {

    public static boolean isOnline(Context context) {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (isM()) {
            Network networks = cm.getActiveNetwork();
            NetworkInfo netInfo = cm.getNetworkInfo(networks);
            haveConnectedWifi = netInfo.getType() == ConnectivityManager.TYPE_WIFI && netInfo.getState().equals(NetworkInfo.State.CONNECTED);
            haveConnectedMobile = netInfo.getType() == ConnectivityManager.TYPE_MOBILE && netInfo.getState().equals(NetworkInfo.State.CONNECTED);
            return haveConnectedWifi || haveConnectedMobile;
        } else {
            NetworkInfo[] netInfo = cm.getAllNetworkInfo();
            for (NetworkInfo ni : netInfo) {
                if (ni.getTypeName().equalsIgnoreCase("WIFI")) {
                    if (ni.isConnected())
                        haveConnectedWifi = true;
                }
                if (ni.getTypeName().equalsIgnoreCase("MOBILE")) {
                    if (ni.isConnected())
                        haveConnectedMobile = true;
                }
            }
            return haveConnectedWifi || haveConnectedMobile;
        }
    }

    public static boolean isM() {return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;}

    public static boolean isLollipopOrHigher() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static boolean isBelowLollipop() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP;
    }

    public static void showKeyboard(@NonNull View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(v, 0);
    }

    public static void hideKeyboard(@NonNull View v) {
        InputMethodManager inputManager = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public static void start(Activity aciActivity, Class cl, View view, String transName) {
        Intent intent = new Intent(aciActivity, cl);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(aciActivity, view, transName);
        if (Build.VERSION.SDK_INT >= 16)
            aciActivity.startActivity(intent, options.toBundle());
        else
            aciActivity.startActivity(intent);
    }

    public static void start(Activity aciActivity, Intent intent, View view, String transName) {
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(aciActivity, view, transName);
        if (Build.VERSION.SDK_INT >= 16)
            aciActivity.startActivity(intent, options.toBundle());
        else
            aciActivity.startActivity(intent);

    }

    public static void startForResult(Activity aciActivity, Class cl, int code, View view, String transName) {
        Intent intent = new Intent(aciActivity, cl);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(aciActivity, view, transName);
        if (Build.VERSION.SDK_INT >= 16)
            aciActivity.startActivityForResult(intent, code, options.toBundle());
        else
            aciActivity.startActivityForResult(intent, code);
    }

    public static void startForResult(Activity aciActivity, Intent cl, int code, View view, String transName) {
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(aciActivity, view, transName);
        if (Build.VERSION.SDK_INT >= 16)
            aciActivity.startActivityForResult(cl, code, options.toBundle());
        else
            aciActivity.startActivityForResult(cl, code);
    }

    public static void startWithExtra(Activity aciActivity, Class cl, Bundle bundle, View view, String transName) {
        Intent intent = new Intent(aciActivity, cl);
        intent.putExtras(bundle);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(aciActivity, view, transName);
        if (Build.VERSION.SDK_INT >= 16)
            aciActivity.startActivity(intent, options.toBundle());
        else
            aciActivity.startActivity(intent);
    }

    @SafeVarargs public static void start(Activity aciActivity, Class cl, Pair<View, String>... sharedElements) {
        Intent intent = new Intent(aciActivity, cl);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(aciActivity, sharedElements);
        if (Build.VERSION.SDK_INT >= 16)
            aciActivity.startActivity(intent, options.toBundle());
        else
            aciActivity.startActivity(intent);
    }

    @SafeVarargs public static void start(Activity aciActivity, Intent intent, Pair<View, String>... sharedElements) {
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(aciActivity, sharedElements);
        if (Build.VERSION.SDK_INT >= 16)
            aciActivity.startActivity(intent, options.toBundle());
        else
            aciActivity.startActivity(intent);

    }

    public static void changeStatusBarColor(@NonNull Activity activity, @ColorInt int color) {
        if (color == 0) return;
        if (isLollipopOrHigher()) {
            float cl = 0.9f;
            float[] hsv = new float[3];
            Color.colorToHSV(color, hsv);
            hsv[2] *= cl;
            int primaryDark = Color.HSVToColor(hsv);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().setStatusBarColor(primaryDark);
        }
    }

    public static boolean isInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }
        return isInBackground;
    }

    public static String saveBitmap(Bitmap image) {
        try {
            File file = FileHelper.generateFile();
            OutputStream fOut = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.JPEG, 70, fOut);
            fOut.flush();
            fOut.close();
            Log.e("PAth", file.getPath());
            return file.getPath();
        } catch (Exception e) {
            return null;
        }
    }

    public static PackageInfo getPackageInfo(Context context, String packageName) {
        try {
            return context.getPackageManager().getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ResolveInfo getResolveInfo(Context context, String packageName) {
        Intent mainIntent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        if (mainIntent != null)
            return context.getPackageManager().resolveActivity(mainIntent, 0);
        return null;
    }

    public static void openAppInfo(Context context, String packageName) {
        try {
            Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + packageName));
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "Settings Can't Be Opened", Toast.LENGTH_SHORT).show();
        }
    }

    public static void uninstallApp(Context context, String packageName) {
        Intent intent = new Intent(Intent.ACTION_DELETE);
        intent.setData(Uri.parse("package:" + packageName));
        context.startActivity(intent);
    }
}
