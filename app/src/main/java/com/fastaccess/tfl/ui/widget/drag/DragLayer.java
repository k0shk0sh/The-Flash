package com.fastaccess.tfl.ui.widget.drag;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import com.fastaccess.tfl.helper.Logger;

public class DragLayer extends WorkspaceLayout implements DragSource, DropTarget {
    private DragController mDragController;
    private DropSpot.OnDragListener onDragListener;

    public DragLayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override public boolean dispatchKeyEvent(KeyEvent event) {
        return mDragController.dispatchKeyEvent(event) || super.dispatchKeyEvent(event);
    }

    @Override public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mDragController.onInterceptTouchEvent(ev);
    }

    @Override public boolean onTouchEvent(MotionEvent ev) {
        return mDragController.onTouchEvent(ev);
    }

    @Override public boolean dispatchUnhandledMove(View focused, int direction) {
        return mDragController.dispatchUnhandledMove(focused, direction);
    }

    @Override public boolean allowDrag() {
        return true;
    }

    @Override public void onDropCompleted(View target, Object mDragInfo, boolean success) {
        log("DragLayer2.onDropCompleted: " + target.getClass().getSimpleName() + " Check that the view moved.");
        onDragListener.onDrop(target, mDragInfo, success);
    }

    @Override public void onDrop(DragSource source, int x, int y, int xOffset, int yOffset, DragView dragView, Object dragInfo) {
    }

    @Override public void onDragEnter(DragSource source, int x, int y, int xOffset, int yOffset, DragView dragView, Object dragInfo) {
        onDragListener.onStart();
    }

    @Override public void onDragOver(DragSource source, int x, int y, int xOffset, int yOffset, DragView dragView, Object dragInfo) {
    }

    @Override public void onDragExit(DragSource source, int x, int y, int xOffset, int yOffset, DragView dragView, Object dragInfo) {
//        if (onDragListener != null) onDragListener.onEnd();
    }

    @Override public boolean acceptDrop(DragSource source, int x, int y, int xOffset, int yOffset, DragView dragView, Object dragInfo) {
        return true;
    }

    @Override
    public Rect estimateDropLocation(DragSource source, int x, int y, int xOffset, int yOffset, DragView dragView, Object dragInfo, Rect recycle) {
        return null;
    }

    public void setDragController(DragController controller, DropSpot.OnDragListener onDragListener) {
        mDragController = controller;
        this.onDragListener = onDragListener;
    }

    public void log(String msg) {
        Logger.e("DragLayer", msg);
    }


}
