package com.fastaccess.tfl.ui.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

/**
 * Created by kosh20111 on 10/8/2015.
 * <p>
 * Viewpager that has scrolling animation by default
 */
public class ViewPagerView extends ViewPager {

    public ViewPagerView(Context context) {
        super(context);
    }

    public ViewPagerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPageTransformer(true, new ViewPagerTransformer());
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        return i;
    }
}
