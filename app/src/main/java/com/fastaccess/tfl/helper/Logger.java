package com.fastaccess.tfl.helper;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Kosh on 04/12/15 11:52 PM.
 */
public class Logger {

    private final static String TAG = "Logger";

    public static void e(@NonNull String tag, @Nullable String text) {
        Log.e(tag, text + "");//avoid null
    }

    public static void e(@NonNull String tag, @Nullable String text, Throwable e) {
        Log.e(tag, text + "", e);//avoid null
    }

    public static void d(@NonNull String tag, @Nullable String text) {
        Log.d(tag, text + "");//avoid null
    }

    public static void i(@NonNull String tag, @Nullable String text) {
        Log.i(tag, text + "");//avoid null
    }

    public static void e(@NonNull String tag, int text) {
        Log.e(tag, text + "");//avoid null
    }

    public static void d(@NonNull String tag, int text) {
        Log.d(tag, text + "");//avoid null
    }

    public static void i(@NonNull String tag, int text) {
        Log.i(tag, text + "");//avoid null
    }

    public static void e(@Nullable String text) {
        Log.e(TAG, text + "");//avoid null
    }

    public static void e(@Nullable String text, Throwable e) {
        Log.e(TAG, text + "", e);//avoid null
    }


    public static void d(@Nullable String text) {
        Log.d(TAG, text + "");//avoid null
    }

    public static void i(@Nullable String text) {
        Log.i(TAG, text + "");//avoid null
    }

    public static void e(@Nullable int text) {
        Log.e(TAG, text + "");//avoid null
    }

    public static void d(@Nullable int text) {
        Log.d(TAG, text + "");//avoid null
    }

    public static void i(@Nullable int text) {
        Log.i(TAG, text + "");//avoid null
    }
}
