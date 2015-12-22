package com.fastaccess.tfl.ui.widget.drag;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.fastaccess.tfl.R;

public class DropSpot extends WorkspaceLayout implements DropTarget, DragController.DragListener {

    public interface OnDragListener {
        void onDrop(View v, Object app, boolean success);

        void onStart();

        void onEnd();
    }

    private DragController mDragController;
    private DragLayer mDragLayer;

    public DropSpot(Context context) {
        super(context);
    }

    public DropSpot(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DropSpot(Context context, AttributeSet attrs, int style) {
        super(context, attrs, style);
    }

    @Override public void onDragStart(DragSource source, Object info, int dragAction) {

    }

    @Override public void onDragEnd() {

    }

    @Override public void onDrop(DragSource source, int x, int y, int xOffset, int yOffset, DragView dragView, Object dragInfo) {

    }

    @Override public void onDragEnter(DragSource source, int x, int y, int xOffset, int yOffset, DragView dragView, Object dragInfo) {

    }

    @Override public void onDragOver(DragSource source, int x, int y, int xOffset, int yOffset, DragView dragView, Object dragInfo) {
        setBackgroundResource(R.drawable.folder_bg);
    }

    @Override public void onDragExit(DragSource source, int x, int y, int xOffset, int yOffset, DragView dragView, Object dragInfo) {
        setBackgroundResource(0);
    }

    @Override public boolean acceptDrop(DragSource source, int x, int y, int xOffset, int yOffset, DragView dragView, Object dragInfo) {
        return isEnabled();
    }

    @Override
    public Rect estimateDropLocation(DragSource source, int x, int y, int xOffset, int yOffset, DragView dragView, Object dragInfo, Rect recycle) {
        return null;
    }

    @Override public boolean isEnabled() {
        return (mDragLayer != null);
    }

    public DragController getDragController() {
        return mDragController;
    }

    public void setDragController(DragController newValue) {
        mDragController = newValue;
    }

    public DragLayer getDragLayer() {
        return mDragLayer;
    }

    public void setDragLayer(DragLayer newValue) {
        mDragLayer = newValue;
    }

    public void setup(DragLayer layer, DragController controller) {
        mDragLayer = layer;
        mDragController = controller;
        if (controller != null) {
            controller.setDragListener(this);
            controller.addDropTarget(this);
        }
    }
}
