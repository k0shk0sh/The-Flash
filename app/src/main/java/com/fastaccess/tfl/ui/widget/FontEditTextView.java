package com.fastaccess.tfl.ui.widget;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.KeyEvent;

import com.fastaccess.tfl.helper.TypeFaceHelper;
import com.fastaccess.tfl.helper.ViewHelper;
import com.fastaccess.tfl.ui.main.drawer.MainDrawerModel;


/**
 * Created by Kosh on 8/18/2015. copyrights are reserved
 */
public class FontEditTextView extends AppCompatEditText {

    private MainDrawerModel mainDrawerModel;

    public FontEditTextView(Context context) {
        super(context);
        init();
    }

    public FontEditTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FontEditTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        if (isInEditMode()) return;
        TypeFaceHelper.applyTypeface(this);
    }

    public void setTextColor(@ColorRes int normalColor, @ColorRes int pressedColor) {
        int nColor = getResources().getColor(normalColor);
        int pColor = getResources().getColor(pressedColor);
        setTextColor(ViewHelper.textSelector(nColor, pColor));
    }

    @Override public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mainDrawerModel != null) {
                mainDrawerModel.onKeyboardHidden();
            }
        }
        return false;
    }

    public void setMainDrawerModel(MainDrawerModel mainDrawerModel) {
        this.mainDrawerModel = mainDrawerModel;
    }
}
