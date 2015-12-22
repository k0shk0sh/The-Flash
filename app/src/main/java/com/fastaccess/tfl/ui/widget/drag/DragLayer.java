/*
 * This is a modified version of a class from the Android Open Source Project. 
 * The original copyright and license information follows.
 * 
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fastaccess.tfl.ui.widget.drag;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import com.fastaccess.tfl.helper.Logger;

/**
 * A ViewGroup that coordinates dragging across its dscendants.
 * <p/>
 * <p> This class used DragLayer in the Android Launcher activity as a model. It is a bit different in several respects: (1) It extends
 * MyAbsoluteLayout rather than FrameLayout; (2) it implements DragSource and DropTarget methods that were done in a separate Workspace class in the
 * Launcher.
 */
public class DragLayer extends MyAbsoluteLayout implements DragSource, DropTarget {
    private DragController mDragController;
    private DropSpot.OnDragListener onDragListener;

    public DragLayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setDragController(DragController controller, DropSpot.OnDragListener onDragListener) {
        mDragController = controller;
        this.onDragListener = onDragListener;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        return mDragController.dispatchKeyEvent(event) || super.dispatchKeyEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mDragController.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return mDragController.onTouchEvent(ev);
    }

    @Override
    public boolean dispatchUnhandledMove(View focused, int direction) {
        return mDragController.dispatchUnhandledMove(focused, direction);
    }

    public boolean allowDrag() {
        return true;
    }

    public void onDropCompleted(View target, Object mDragInfo, boolean success) {
        toast("DragLayer2.onDropCompleted: " + target.getClass().getSimpleName() + " Check that the view moved.");
        onDragListener.onDrop(target, mDragInfo, success);
    }

    public void onDrop(DragSource source, int x, int y, int xOffset, int yOffset, DragView dragView, Object dragInfo) {
    }

    public void onDragEnter(DragSource source, int x, int y, int xOffset, int yOffset, DragView dragView, Object dragInfo) {
        onDragListener.onStart();
    }

    public void onDragOver(DragSource source, int x, int y, int xOffset, int yOffset, DragView dragView, Object dragInfo) {
    }

    public void onDragExit(DragSource source, int x, int y, int xOffset, int yOffset, DragView dragView, Object dragInfo) {
//        if (onDragListener != null) onDragListener.onEnd();
    }

    public boolean acceptDrop(DragSource source, int x, int y, int xOffset, int yOffset, DragView dragView, Object dragInfo) {
        return true;
    }

    public Rect estimateDropLocation(DragSource source, int x, int y, int xOffset, int yOffset, DragView dragView, Object dragInfo, Rect recycle) {
        return null;
    }

    public void toast(String msg) {
        Logger.e("DragLayer", msg);
    }


}
