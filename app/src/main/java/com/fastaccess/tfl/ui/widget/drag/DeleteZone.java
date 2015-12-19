package com.fastaccess.tfl.ui.widget.drag;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * This class describes an area within a DragLayer where a dragged item can be dropped in order to remove it from the screen. It is a subclass of
 * ImageView so it is easy to make the area appear as a trash icon or whatever you like.
 * <p>
 * <p> The default implementation assumes that the ImageView supports image levels. Image level 1 is the normal view. Level 2 is for use when the
 * DeleteZone has a dragged object over it. To change that behavior, override methods onDragEnter and onDragExit.
 */

public class DeleteZone extends ImageView implements DropTarget {

    public DeleteZone(Context context) {
        super(context);
    }

    public DeleteZone(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DeleteZone(Context context, AttributeSet attrs, int style) {
        super(context, attrs, style);
    }

    private DragController mDragController;
    private boolean mEnabled = true;

    public DragController getDragController() {
        return mDragController;
    } // end getDragController

    public void setDragController(DragController newValue) {
        mDragController = newValue;
    } // end setDragController

    public void onDrop(DragSource source, int x, int y, int xOffset, int yOffset, DragView dragView, Object dragInfo) {
        if (isEnabled()) toast("Moved to trash.");

    }

    public void onDragEnter(DragSource source, int x, int y, int xOffset, int yOffset, DragView dragView, Object dragInfo) {
        // Set the image level so the image is highlighted;
        if (isEnabled()) setImageLevel(2);
    }

    public void onDragOver(DragSource source, int x, int y, int xOffset, int yOffset,
                           DragView dragView, Object dragInfo) {
    }

    public void onDragExit(DragSource source, int x, int y, int xOffset, int yOffset, DragView dragView, Object dragInfo) {
        if (isEnabled()) setImageLevel(1);
    }

    public boolean acceptDrop(DragSource source, int x, int y, int xOffset, int yOffset, DragView dragView, Object dragInfo) {
        return isEnabled();
    }

    public Rect estimateDropLocation(DragSource source, int x, int y, int xOffset, int yOffset, DragView dragView, Object dragInfo, Rect recycle) {
        return null;
    }

    public boolean isEnabled() {
        return mEnabled && (getVisibility() == View.VISIBLE);
    } // end getDragLayer

    public void setup(DragController controller) {
        mDragController = controller;

        if (controller != null) {
            controller.addDropTarget(this);
        }
    }

    public void toast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    } // end toast


}
