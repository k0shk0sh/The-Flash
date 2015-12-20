package com.fastaccess.tfl.core;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.appcompat.BuildConfig;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import butterknife.ButterKnife;
import icepick.Icepick;

/**
 * Created by Kosh on 05/12/15 11:50 AM
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected abstract int layoutResId();

    @Nullable protected abstract Toolbar toolbar();

    @Override protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT_WATCH) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setNavigationBarTintEnabled(true);
            tintManager.setStatusBarTintColor(Color.TRANSPARENT);
        }
        setContentView(layoutResId());
        ButterKnife.bind(this);
        Icepick.setDebug(BuildConfig.DEBUG);
        if (savedInstanceState != null && !savedInstanceState.isEmpty()) {
            Icepick.restoreInstanceState(this, savedInstanceState);
        }
        if (toolbar() != null) {
            setSupportActionBar(toolbar());
        }
    }
}
