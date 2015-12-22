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
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.v4.view.GravityCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

public class DragView extends View {
    private static final int DRAG_SCALE = 0;
    private Bitmap mBitmap;
    private Paint mPaint;
    private int mRegistrationX;
    private int mRegistrationY;
    private float mScale;
    private float mAnimationScale = 1.0f;
    private WindowManager.LayoutParams mLayoutParams;
    private WindowManager mWindowManager;

    public DragView(Context context) {
        super(context);
    }

    public DragView(Context context, Bitmap bitmap, int registrationX, int registrationY, int left, int top, int width, int height) {
        super(context);
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Matrix scale = new Matrix();
        float scaleFactor = width;
        scaleFactor = mScale = (scaleFactor + DRAG_SCALE) / scaleFactor;
        scale.setScale(scaleFactor, scaleFactor);
        mBitmap = Bitmap.createBitmap(bitmap, left, top, width, height, scale, true);
        mRegistrationX = registrationX + (DRAG_SCALE / 2);
        mRegistrationY = registrationY + (DRAG_SCALE / 2);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(mBitmap.getWidth(), mBitmap.getHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float scale = mAnimationScale;
        if (scale < 0.999f) {
            float width = mBitmap.getWidth();
            float offset = (width - (width * scale)) / 2;
            canvas.translate(offset, offset);
            canvas.scale(scale, scale);
        }
        canvas.drawBitmap(mBitmap, 0.0f, 0.0f, mPaint);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mBitmap.recycle();
    }

    public void setPaint(Paint paint) {
        mPaint = paint;
        invalidate();
    }

    public void setScale(float scale) {
        if (scale > 1.0f) mAnimationScale = 1.0f;
        else mAnimationScale = scale;
        invalidate();
    }

    public void show(IBinder windowToken, int touchX, int touchY) {
        WindowManager.LayoutParams lp;
        int pixelFormat;
        pixelFormat = PixelFormat.TRANSLUCENT;
        lp = new WindowManager.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                touchX - mRegistrationX, touchY - mRegistrationY,
                WindowManager.LayoutParams.TYPE_APPLICATION_SUB_PANEL,
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                pixelFormat);
        lp.gravity = GravityCompat.START | Gravity.TOP;
        lp.token = windowToken;
        lp.setTitle("DragView");
        mLayoutParams = lp;
        mWindowManager.addView(this, lp);
    }

    void move(int touchX, int touchY) {
        WindowManager.LayoutParams lp = mLayoutParams;
        lp.x = touchX - mRegistrationX;
        lp.y = touchY - mRegistrationY;
        mWindowManager.updateViewLayout(this, lp);
    }

    void remove() {
        mWindowManager.removeView(this);
    }
}

