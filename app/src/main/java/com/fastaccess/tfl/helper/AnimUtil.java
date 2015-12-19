package com.fastaccess.tfl.helper;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;

import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;

/**
 * Created by Kosh on 23/11/15 12:15 PM
 */
public class AnimUtil {

    public static void circularReveal(final View mRevealView, final boolean show) {
        if (ViewCompat.isAttachedToWindow(mRevealView)) {
            circular(mRevealView, show);
        } else {
            mRevealView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override public boolean onPreDraw() {
                    mRevealView.getViewTreeObserver().removeOnPreDrawListener(this);
                    circular(mRevealView, show);
                    return true;
                }
            });
        }
    }

    public static void circular(final View mRevealView, final boolean show) {
        int cx = (mRevealView.getLeft() + mRevealView.getRight());
        int cy = (mRevealView.getTop() + mRevealView.getBottom()) / 2;
//        int cy = mRevealView.getTop();
        int radius = Math.max(mRevealView.getWidth(), mRevealView.getHeight());
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            SupportAnimator animator = ViewAnimationUtils.createCircularReveal(mRevealView, cx, cy, 0, radius);
            animator.setInterpolator(new AccelerateDecelerateInterpolator());
            animator.setDuration(600);
            SupportAnimator animator_reverse = animator.reverse();
            if (animator_reverse != null) {
                if (show) {
                    mRevealView.setVisibility(View.VISIBLE);
                    animator.start();
                } else {
                    animator_reverse.addListener(new SupportAnimator.AnimatorListener() {
                        @Override
                        public void onAnimationStart() {}

                        @Override
                        public void onAnimationEnd() {
                            mRevealView.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onAnimationCancel() {}

                        @Override
                        public void onAnimationRepeat() {}
                    });
                    animator_reverse.start();
                }
            }
        } else {
            if (show) {
                Animator anim = android.view.ViewAnimationUtils.createCircularReveal(mRevealView, cx, cy, 0, radius);
                mRevealView.setVisibility(View.VISIBLE);
                anim.start();
            } else {
                Animator anim = android.view.ViewAnimationUtils.createCircularReveal(mRevealView, cx, cy, radius, 0);
                anim.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        mRevealView.setVisibility(View.INVISIBLE);
                    }
                });
                anim.start();

            }
        }
    }

    public static void circularRevealFromBottom(final View mRevealView, final boolean show) {
        if (ViewCompat.isAttachedToWindow(mRevealView)) {
            fromBottom(mRevealView, show);
        } else {
            mRevealView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override public boolean onPreDraw() {
                    mRevealView.getViewTreeObserver().removeOnPreDrawListener(this);
                    fromBottom(mRevealView, show);
                    return true;
                }
            });
        }
    }

    public static void circularRevealFromBottom(final View mRevealView, final View btn, final boolean show) {
        if (ViewCompat.isAttachedToWindow(mRevealView)) {
            fromBottom(mRevealView, btn, show);
        } else {
            mRevealView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override public boolean onPreDraw() {
                    mRevealView.getViewTreeObserver().removeOnPreDrawListener(this);
                    fromBottom(mRevealView, btn, show);
                    return true;
                }
            });
        }
    }

    private static void fromBottom(final View mRevealView, final boolean show) {
        int cx = (mRevealView.getLeft() + mRevealView.getRight()) / 2;
        int cy = mRevealView.getBottom();
        int radius = Math.max(mRevealView.getWidth(), mRevealView.getHeight());
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            SupportAnimator animator = ViewAnimationUtils.createCircularReveal(mRevealView, cx, cy, 0, radius);
            animator.setInterpolator(new AccelerateDecelerateInterpolator());
            animator.setDuration(600);
            SupportAnimator animator_reverse = animator.reverse();
            if (animator_reverse != null) {
                if (show) {
                    mRevealView.setVisibility(View.VISIBLE);
                    animator.start();
                } else {
                    animator_reverse.addListener(new SupportAnimator.AnimatorListener() {
                        @Override
                        public void onAnimationStart() {}

                        @Override
                        public void onAnimationEnd() {
                            mRevealView.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onAnimationCancel() {}

                        @Override
                        public void onAnimationRepeat() {}
                    });
                    animator_reverse.start();
                }
            }
        } else {
            if (show) {
                Animator anim = android.view.ViewAnimationUtils.createCircularReveal(mRevealView, cx, cy, 0, radius);
                mRevealView.setVisibility(View.VISIBLE);
                anim.start();
            } else {
                Animator anim = android.view.ViewAnimationUtils.createCircularReveal(mRevealView, cx, cy, radius, 0);
                anim.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        mRevealView.setVisibility(View.INVISIBLE);
                    }
                });
                anim.start();

            }
        }
    }

    private static void fromBottom(final View mRevealView, View btn, final boolean show) {
        int cx = (btn.getLeft() + btn.getRight()) / 2;
        int cy = (mRevealView.getTop() + mRevealView.getBottom());
        int radius = Math.max(mRevealView.getWidth(), mRevealView.getHeight());
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            SupportAnimator animator = ViewAnimationUtils.createCircularReveal(mRevealView, cx, cy, 0, radius);
            animator.setInterpolator(new AccelerateDecelerateInterpolator());
            animator.setDuration(600);
            SupportAnimator animator_reverse = animator.reverse();
            if (animator_reverse != null) {
                if (show) {
                    mRevealView.setVisibility(View.VISIBLE);
                    animator.start();
                } else {
                    animator_reverse.addListener(new SupportAnimator.AnimatorListener() {
                        @Override
                        public void onAnimationStart() {}

                        @Override
                        public void onAnimationEnd() {
                            mRevealView.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onAnimationCancel() {}

                        @Override
                        public void onAnimationRepeat() {}
                    });
                    animator_reverse.start();
                }
            }
        } else {
            if (show) {
                Animator anim = android.view.ViewAnimationUtils.createCircularReveal(mRevealView, cx, cy, 0, radius);
                mRevealView.setVisibility(View.VISIBLE);
                anim.start();
            } else {
                Animator anim = android.view.ViewAnimationUtils.createCircularReveal(mRevealView, cx, cy, radius, 0);
                anim.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        mRevealView.setVisibility(View.INVISIBLE);
                    }
                });
                anim.start();

            }
        }
    }

}
