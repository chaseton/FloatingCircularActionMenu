package com.stardust.floatingcircularactionmenu.gesture;

import android.animation.ValueAnimator;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.BounceInterpolator;

import com.stardust.enhancedfloaty.WindowBridge;

/**
 * Created by Stardust on 2017/9/26.
 */

public class BounceDragGesture extends DragGesture {

    private long mBounceDuration = 300;
    private BounceInterpolator mBounceInterpolator;

    public BounceDragGesture(WindowBridge windowBridge, View view) {
        super(windowBridge, view);
        setAutoKeepToEdge(true);
        mBounceInterpolator = new BounceInterpolator();
    }

    public void setBounceDuration(long bounceDuration) {
        mBounceDuration = bounceDuration;
    }

    @Override
    public boolean onDown(MotionEvent event) {
        return super.onDown(event);
    }

    @Override
    public void keepToEdge() {
        int x = mWindowBridge.getX();
        int hiddenWidth = (int) (getKeepToSideHiddenWidthRadio() * (float) mView.getWidth());
        if (x > mWindowBridge.getScreenWidth() / 2) {
            bounce(x, mWindowBridge.getScreenWidth() - mView.getWidth() + hiddenWidth);
        } else {
            bounce(x, -hiddenWidth);
        }
    }

    protected void bounce(final int fromX, final int toX) {
        ValueAnimator animator = ValueAnimator.ofFloat(fromX, toX);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mWindowBridge.updatePosition((int) ((float) animation.getAnimatedValue()), mWindowBridge.getY());
            }
        });
        animator.setDuration(mBounceDuration);
        animator.setInterpolator(new BounceInterpolator());
        animator.start();
    }
}
