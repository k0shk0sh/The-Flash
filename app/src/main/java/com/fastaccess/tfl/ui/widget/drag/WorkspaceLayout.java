package com.fastaccess.tfl.ui.widget.drag;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RemoteViews.RemoteView;

@RemoteView public class WorkspaceLayout extends ViewGroup {
    public WorkspaceLayout(Context context) {
        super(context);
    }

    public WorkspaceLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WorkspaceLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = getChildCount();
        int maxHeight = 0;
        int maxWidth = 0;
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                int childRight;
                int childBottom;
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                childRight = lp.x + child.getMeasuredWidth();
                childBottom = lp.y + child.getMeasuredHeight();
                maxWidth = Math.max(maxWidth, childRight);
                maxHeight = Math.max(maxHeight, childBottom);
            }
        }
        maxWidth += getPaddingLeft() + getPaddingRight();
        maxHeight += getPaddingTop() + getPaddingBottom();
        maxHeight = Math.max(maxHeight, getSuggestedMinimumHeight());
        maxWidth = Math.max(maxWidth, getSuggestedMinimumWidth());
        setMeasuredDimension(resolveSize(maxWidth, widthMeasureSpec), resolveSize(maxHeight, heightMeasureSpec));
    }

    @Override protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 0, 0);
    }

    @Override protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();

        int paddingL = getPaddingLeft();
        int paddingT = getPaddingTop();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != GONE) {

                LayoutParams lp =
                        (LayoutParams) child.getLayoutParams();

                int childLeft = paddingL + lp.x;
                int childTop = paddingT + lp.y;
                /*
                int childLeft = mPaddingLeft + lp.x;
                int childTop = mPaddingTop + lp.y;
                */
                child.layout(childLeft, childTop,
                        childLeft + child.getMeasuredWidth(),
                        childTop + child.getMeasuredHeight());

            }
        }
    }

    @Override public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    @Override protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    public static class LayoutParams extends ViewGroup.LayoutParams {
        public int x;
        public int y;

        public LayoutParams(int width, int height, int x, int y) {
            super(width, height);
            this.x = x;
            this.y = y;
        }

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            /* FIX THIS eventually. Without this, I don't think you can put x and y in layout xml files.
            TypedArray a = c.obtainStyledAttributes(attrs,
                    com.android.internal.R.styleable.AbsoluteLayout_Layout);
            x = a.getDimensionPixelOffset(
                    com.android.internal.R.styleable.AbsoluteLayout_Layout_layout_x, 0);
            y = a.getDimensionPixelOffset(
                    com.android.internal.R.styleable.AbsoluteLayout_Layout_layout_y, 0);
            a.recycle();
            */
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        public String debug(String output) {
            return output + "Absolute.LayoutParams={width="
                    + sizeToString(width) + ", height=" + sizeToString(height)
                    + " x=" + x + " y=" + y + "}";
        }

        protected static String sizeToString(int size) {
            if (size == WRAP_CONTENT) {
                return "wrap-content";
            }
            if (size == MATCH_PARENT) {
                return "match-parent";
            }
            return String.valueOf(size);
        }
    }

}


