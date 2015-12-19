package com.fastaccess.tfl.helper;

import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * Created by Kosh on 19/12/15 1:32 PM
 */
public abstract class GestureHelper extends GestureDetector.SimpleOnGestureListener {
    private static final int MAX_VELOCITY_RATIO = 3;
    private static final int MIN_SWIPE_DISTANCE = 100;

    @Override public boolean onSingleTapUp(MotionEvent e) {
        onClick();
        return true;
    }

    @Override public void onLongPress(MotionEvent e) {
        onLongClick();
    }

    @Override public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return super.onScroll(e1, e2, distanceX, distanceY);
    }

    @Override public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        int dx = (int) (e2.getX() - e1.getX());
        int dy = (int) (e2.getY() - e1.getY());
        if (Math.abs(dx) > MIN_SWIPE_DISTANCE && Math.abs(velocityX) > MAX_VELOCITY_RATIO * Math.abs(velocityY)) {
            if (velocityX > 0) {
                onSwipeRight();
            } else {
                onSwipeLeft();
            }
            return true;
        } else if (Math.abs(dy) > MIN_SWIPE_DISTANCE && Math.abs(velocityY) > MAX_VELOCITY_RATIO * Math.abs(velocityX)) {
            if (velocityY > 0) {
                onSwipeDown();
            } else {
                onSwipeUp();
            }
            return true;
        }
        return false;
    }

    @Override public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override public boolean onDoubleTap(MotionEvent e) {
        onDoubleClick();
        return true;
    }

    @Override public boolean onDoubleTapEvent(MotionEvent e) {
        return super.onDoubleTapEvent(e);
    }

    @Override public boolean onSingleTapConfirmed(MotionEvent e) {
        return super.onSingleTapConfirmed(e);
    }

    protected abstract void onClick();

    protected abstract void onDoubleClick();

    protected abstract void onLongClick();

    protected abstract void onSwipeRight();

    protected abstract void onSwipeLeft();

    protected abstract void onSwipeUp();

    protected abstract void onSwipeDown();

    public static class SimpleGestureHelper extends GestureHelper {

        @Override protected void onClick() {

        }

        @Override protected void onDoubleClick() {

        }

        @Override protected void onLongClick() {

        }

        @Override protected void onSwipeRight() {

        }

        @Override protected void onSwipeLeft() {

        }

        @Override protected void onSwipeUp() {

        }

        @Override protected void onSwipeDown() {

        }
    }
}
