package com.fastaccess.tfl.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by kosh20111 on 10/7/2015.
 */
public class PrefHelper {

    private static Context context;

    public static void init(Context context) {
        PrefHelper.context = context;
    }

    /**
     * @param key
     *         ( the Key to used to retrieve this data later  )
     * @param value
     *         ( any kind of primitive values  )
     *         <p/>
     *         non can be null!!!
     */
    public static void set(String key, Object value) {
        if (key == null || value == null) {
            throw new NullPointerException("Key || Value must not be null! (key = " + key + "), (value = " + key + ")");
        }
        SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(context).edit();
        if (value instanceof String) {
            edit.putString(key, (String) value);
        } else if (value instanceof Integer) {
            edit.putInt(key, (int) value);
        } else if (value instanceof Long) {
            edit.putLong(key, (long) value);
        } else if (value instanceof Boolean) {
            edit.putBoolean(key, (boolean) value);
        } else if (value instanceof Float) {
            edit.putFloat(key, (float) value);
        } else {
            throw new IllegalArgumentException("Value must be in one of these {String, int, float, long, boolean} given value is{ " + value
                    .getClass().getSimpleName() + "}");
        }
        edit.apply();
    }

    public static String getString(String key) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(key, null);
    }

    public static boolean getBoolean(String key) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(key, false);
    }

    public static int getInt(String key) {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt(key, 0);
    }

    public static long getLong(String key) {
        return PreferenceManager.getDefaultSharedPreferences(context).getLong(key, 0);
    }

    public static float getFloat(String key) {
        return PreferenceManager.getDefaultSharedPreferences(context).getFloat(key, 0);
    }

    public static void clearKey(String key) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().remove(key).apply();
    }

    public static boolean isExist(String key) {
        return PreferenceManager.getDefaultSharedPreferences(context).contains(key);
    }

    public static void clearPrefs(Context context) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().clear().apply();
    }
}
