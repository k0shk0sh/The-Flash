/*
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
import android.view.View;

import com.fastaccess.tfl.helper.Logger;

/**
 * This class describes an area within a DragLayer where a dragged item can be dropped. It is a subclass of MyAbsoluteLayout, which means that it is a
 * ViewGroup and views can be added as child views.
 * <p>
 * <p> In the onDrop method, the view dropped is not added as a child view. Instead, the view is repositioned within the DragLayer that contains the
 * DropSpot. If the DropSpot is not associated with a DragLayer, it will not accept dropped objects.
 */

public class DropSpot extends MyAbsoluteLayout implements DropTarget, DragController.DragListener {

    public interface OnDragLisenter {
        void onDrop(View v, View where);

        void onStart();
    }

    private OnDragLisenter onDragLisenter;

    public DropSpot(Context context) {
        super(context);
    }

    public DropSpot(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DropSpot(Context context, AttributeSet attrs, int style) {
        super(context, attrs, style);
    }

    private DragController mDragController;
    private DragLayer mDragLayer;

    public DragController getDragController() {
        return mDragController;
    } // end getDragController

    public void setDragController(DragController newValue) {
        mDragController = newValue;
    } // end setDragController

    public DragLayer getDragLayer() {
        return mDragLayer;
    } // end getDragLayer

    public void setDragLayer(DragLayer newValue) {
        mDragLayer = newValue;
    } // end setDragLayer

    public int getSavedBackground() {
        //if (mSavedBackground == null) {}
        return 0;
    } // end getSavedBackground

    public void setSavedBackground(int newValue) {

    } // end setSavedBackground

    public void onDragStart(DragSource source, Object info, int dragAction) {
        onDragLisenter.onStart();
        toast("onDragStart");
    }

    public void onDragEnd() {
        toast("onDragEnd");
    }

    public void onDrop(DragSource source, int x, int y, int xOffset, int yOffset, DragView dragView, Object dragInfo) {
        // We need the DragLayer to do the drop.
        if (mDragLayer == null) return;

        // The view being moved is stored in the dragInfo.
        View v = (View) dragInfo;

        // At some point, it would be nice to get away from use of the clone of deprecated AbsoluteLayout.
        // That's what DragLayer is defined in terms of.

        // Launcher code and my own DragView moves the DragView this way.
        // Don't think this is the way.
        // Go back and study the Launcher code some more. Look at CellLayout.
    /*
    WindowManager.LayoutParams lp = mLayoutParams;
    lp.x = touchX - mRegistrationX;
    lp.y = touchY - mRegistrationY;
    mWindowManager.updateViewLayout(this, lp);
    */

        // For now, take the view and update its position using the DragLayer layout object.
        // The coordinates given to onDrop are relative to the DropSpot. Add in its x and y location
        // so the drop occurs where the user expects it.
        // (At least the fact that the layout is absolute is pretty much hidden from the caller.)
        int viewX = this.getLeft();
        int viewY = this.getTop();
        int w = v.getWidth();
        int h = v.getHeight();
        int left = x - xOffset + viewX;
        int top = y - yOffset + viewY;
        onDragLisenter.onDrop(v, (View) dragInfo);
    }

    public void onDragEnter(DragSource source, int x, int y, int xOffset, int yOffset, DragView dragView, Object dragInfo) {
        toast("onDragEnter");
    }

    public void onDragOver(DragSource source, int x, int y, int xOffset, int yOffset, DragView dragView, Object dragInfo) {
    }

    public void onDragExit(DragSource source, int x, int y, int xOffset, int yOffset, DragView dragView, Object dragInfo) {
        toast("onDragExit");
        setBackgroundResource(getSavedBackground());
    }

    public boolean acceptDrop(DragSource source, int x, int y, int xOffset, int yOffset, DragView dragView, Object dragInfo) {
        return isEnabled();
    }

    public Rect estimateDropLocation(DragSource source, int x, int y, int xOffset, int yOffset, DragView dragView, Object dragInfo, Rect recycle) {
        return null;
    }

    public boolean isEnabled() {
        return (mDragLayer != null);
    } // end getDragLayer

    public void setup(DragLayer layer, DragController controller, OnDragLisenter onDragLisenter) {
        mDragLayer = layer;
        mDragController = controller;
        this.onDragLisenter = onDragLisenter;
        if (controller != null) {
            controller.setDragListener(this);
            controller.addDropTarget(this);
        }
    }

    public void toast(String msg) {
        Logger.e(msg);
    } // end toast


} // end DropSpot
