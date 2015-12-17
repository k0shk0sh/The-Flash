package com.fastaccess.tfl.helper;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;

/**
 * Created by Kosh on 23/11/15 12:15 PM
 */
public class AnimUtil {

    public interface OnAnimationListener {
        void onAnimFinished();
    }

    public static void circularReveal(final View mRevealView, final boolean show) {
        if (ViewCompat.isAttachedToWindow(mRevealView)) {
            circularReveal(null, mRevealView, show);
        } else {
            mRevealView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override public boolean onPreDraw() {
                    mRevealView.getViewTreeObserver().removeOnPreDrawListener(this);
                    circularReveal(null, mRevealView, show);
                    return true;
                }
            });
        }
    }

    public static void circularRevealWithChecking(final OnAnimationListener onAnimationLisenter, final View mRevealView, final boolean show) {
        if (ViewCompat.isAttachedToWindow(mRevealView)) {
            circularReveal(onAnimationLisenter, mRevealView, show);
        } else {
            mRevealView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override public boolean onPreDraw() {
                    mRevealView.getViewTreeObserver().removeOnPreDrawListener(this);
                    circularReveal(onAnimationLisenter, mRevealView, show);
                    return true;
                }
            });
        }
    }

    public static void circularRevealFromTop(final View mRevealView, final boolean show) {
        circularRevealFromTop(null, mRevealView, show);
    }

    public static void circularReveal(final OnAnimationListener onAnimationLisenter, final View mRevealView, final boolean show) {
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
                            if (onAnimationLisenter != null) onAnimationLisenter.onAnimFinished();
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
                anim.addListener(new AnimatorListenerAdapter() {
                    @Override public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        if (onAnimationLisenter != null) onAnimationLisenter.onAnimFinished();
                    }
                });
                anim.start();
            } else {
                Animator anim = android.view.ViewAnimationUtils.createCircularReveal(mRevealView, cx, cy, radius, 0);
                anim.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        mRevealView.setVisibility(View.INVISIBLE);
                        if (onAnimationLisenter != null) onAnimationLisenter.onAnimFinished();
                    }
                });
                anim.start();

            }
        }
    }

    public static void circularRevealFromTop(final OnAnimationListener onAnimationLisenter, final View mRevealView, final boolean show) {
        if (ViewCompat.isAttachedToWindow(mRevealView)) {
            doAnim(null, mRevealView, show);
        } else {
            mRevealView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override public boolean onPreDraw() {
                    mRevealView.getViewTreeObserver().removeOnPreDrawListener(this);
                    doAnim(null, mRevealView, show);
                    return true;
                }
            });
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

    public static void circularRevealFromBottom(final OnAnimationListener onAnimationLisenter, final View mRevealView, final boolean show) {
        if (ViewCompat.isAttachedToWindow(mRevealView)) {
            fromBottom(onAnimationLisenter, mRevealView, show);
        } else {
            mRevealView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override public boolean onPreDraw() {
                    mRevealView.getViewTreeObserver().removeOnPreDrawListener(this);
                    fromBottom(onAnimationLisenter, mRevealView, show);
                    return true;
                }
            });
        }
    }

    private static void fromBottom(final OnAnimationListener onAnimationLisenter, final View mRevealView, final boolean show) {
        int cx = (mRevealView.getLeft() + mRevealView.getRight());
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
                    animator.addListener(new SupportAnimator.AnimatorListener() {
                        @Override public void onAnimationStart() {

                        }

                        @Override public void onAnimationEnd() {
                            if (onAnimationLisenter != null) onAnimationLisenter.onAnimFinished();
                        }

                        @Override public void onAnimationCancel() {

                        }

                        @Override public void onAnimationRepeat() {

                        }
                    });
                } else {
                    animator_reverse.addListener(new SupportAnimator.AnimatorListener() {
                        @Override
                        public void onAnimationStart() {}

                        @Override
                        public void onAnimationEnd() {
                            mRevealView.setVisibility(View.INVISIBLE);
                            if (onAnimationLisenter != null) onAnimationLisenter.onAnimFinished();
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
                anim.addListener(new AnimatorListenerAdapter() {
                    @Override public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        if (onAnimationLisenter != null) onAnimationLisenter.onAnimFinished();
                    }
                });
                anim.start();
            } else {
                Animator anim = android.view.ViewAnimationUtils.createCircularReveal(mRevealView, cx, cy, radius, 0);
                anim.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        mRevealView.setVisibility(View.INVISIBLE);
                        if (onAnimationLisenter != null) onAnimationLisenter.onAnimFinished();
                    }
                });
                anim.start();

            }
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

    public static void circularRevealFromTopLong(final View mRevealView) {
        if (ViewCompat.isAttachedToWindow(mRevealView)) {
            Log.e("Attached", ViewCompat.isAttachedToWindow(mRevealView) + "");
            doAnim(mRevealView, 8000);
        } else {
            Log.e("NotAttached", ViewCompat.isAttachedToWindow(mRevealView) + "");
            mRevealView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override public boolean onPreDraw() {
                    mRevealView.getViewTreeObserver().removeOnPreDrawListener(this);
                    doAnim(mRevealView, 8000);
                    return true;
                }
            });
        }
    }

    public static void circularRevealFromBottomLong(final View mRevealView) {
        if (ViewCompat.isAttachedToWindow(mRevealView)) {
            Log.e("Attached", ViewCompat.isAttachedToWindow(mRevealView) + "");
            doAnim(mRevealView, 8000);
        } else {
            Log.e("NotAttached", ViewCompat.isAttachedToWindow(mRevealView) + "");
            mRevealView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override public boolean onPreDraw() {
                    mRevealView.getViewTreeObserver().removeOnPreDrawListener(this);
                    doAnim(mRevealView, 8000);
                    return true;
                }
            });
        }
    }

    private static void doAnim(final View mRevealView, final long duration) {
        if (AppHelper.isBelowLollipop()) {
            Animation animation = AnimationUtils.loadAnimation(mRevealView.getContext(), android.R.anim.fade_out);
            animation.setDuration(duration);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    mRevealView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mRevealView.setVisibility(View.GONE);
                    mRevealView.clearAnimation();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            mRevealView.startAnimation(animation);
        } else {
            final int cx = (mRevealView.getLeft() + mRevealView.getRight());
            final int cy = mRevealView.getTop();
            final int radius = Math.max(mRevealView.getWidth(), mRevealView.getHeight());
            final Animator start = android.view.ViewAnimationUtils.createCircularReveal(mRevealView, cx, cy, 0, radius);
            mRevealView.setVisibility(View.VISIBLE);
            start.addListener(new AnimatorListenerAdapter() {
                @Override public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    final Animator anim = android.view.ViewAnimationUtils.createCircularReveal(mRevealView, cx, cy, radius, 0);
                    anim.setStartDelay(duration);
                    anim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            mRevealView.setVisibility(View.INVISIBLE);
                            start.addListener(null);
                            anim.addListener(null);
                        }
                    });
                    anim.start();
                }
            });
            start.start();
        }
    }

    private static void doAnim(final OnAnimationListener onAnimationLisenter, final View mRevealView, final boolean show) {
        int cx = (mRevealView.getLeft() + mRevealView.getRight());
//        int cy = (mRevealView.getTop() + mRevealView.getBottom()) / 2;
        int cy = mRevealView.getTop();
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
                            if (onAnimationLisenter != null) onAnimationLisenter.onAnimFinished();
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
                        if (onAnimationLisenter != null) onAnimationLisenter.onAnimFinished();
                    }
                });
                anim.start();

            }
        }
    }
}
