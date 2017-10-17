package com.stardust.floatingcircularactionmenu;

import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.stardust.enhancedfloaty.FloatyService;
import com.stardust.enhancedfloaty.FloatyWindow;
import com.stardust.enhancedfloaty.WindowBridge;
import com.stardust.floatingcircularactionmenu.gesture.BounceDragGesture;

/**
 * Created by Stardust on 2017/9/25.
 */

public class CircularActionMenuFloatingWindow implements FloatyWindow {

    private CircularActionMenuFloaty mFloaty;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mWindowLayoutParams;
    private CircularActionMenu mCircularActionMenu;
    private View mCircularActionView;
    private BounceDragGesture mDragGesture;
    private WindowBridge mWindowBridge;


    public CircularActionMenuFloatingWindow(CircularActionMenuFloaty floaty) {
        mFloaty = floaty;
    }

    @Override
    public void onCreate(FloatyService service, WindowManager manager) {
        mWindowManager = manager;
        mWindowLayoutParams = createWindowLayoutParams();
        inflateWindowViews(service);
        initWindowView(service);
        initWindowBridge();
        initGestures();
        setKeyListener();
        setInitialState();
    }

    private void setInitialState() {
        mDragGesture.keepToEdge();
    }

    private void initGestures() {
        mDragGesture = new BounceDragGesture(mWindowBridge, mCircularActionView) {

            @Override
            public void keepToEdge() {
                int menuCenterX = mWindowBridge.getX() + mCircularActionMenu.getMeasuredWidth() / 2;
                int hiddenWidth = (int) (getKeepToSideHiddenWidthRadio() * (float) mCircularActionView.getMeasuredWidth());
                if (menuCenterX > mWindowBridge.getScreenWidth() / 2) {
                    bounce(menuCenterX, mWindowBridge.getScreenWidth() - mCircularActionMenu.getExpandedWidth() + hiddenWidth);
                } else {
                    bounce(menuCenterX, -mCircularActionMenu.getExpandedWidth() + mCircularActionView.getMeasuredWidth() - hiddenWidth);
                }
            }

            @Override
            protected boolean onTheEdge() {
                int menuCenterX = mWindowBridge.getX() + mCircularActionMenu.getMeasuredWidth() / 2;
                int dX1 = Math.abs(menuCenterX);
                int dX2 = Math.abs(menuCenterX - mWindowBridge.getScreenWidth());
                int d = Math.min(dX1, dX2) - mCircularActionView.getMeasuredWidth() / 2;
                return Math.abs(d) < 5;
            }
        };
        mDragGesture.setKeepToSideHiddenWidthRadio(0);
    }

    private void initWindowBridge() {
        mWindowBridge = new WindowBridge.DefaultImpl(mWindowLayoutParams, mWindowManager, mCircularActionView);
    }

    private WindowManager.LayoutParams createWindowLayoutParams() {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                PixelFormat.TRANSLUCENT);
        layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        return layoutParams;
    }

    private void setKeyListener() {
        mDragGesture.setOnDraggedViewClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCircularActionMenu.isExpanded()) {
                    mCircularActionMenu.collapse();
                } else {
                    if (mWindowBridge.getX() > mWindowBridge.getScreenWidth() / 2) {
                        mCircularActionMenu.expand(Gravity.LEFT);
                    } else {
                        mCircularActionMenu.expand(Gravity.RIGHT);
                    }
                }
            }
        });
    }

    private void initWindowView(FloatyService service) {

    }

    private void inflateWindowViews(FloatyService service) {
        mCircularActionMenu = mFloaty.inflateMenuItems(service, this);
        mCircularActionView = mFloaty.inflateActionView(service, this);
        mCircularActionMenu.setVisibility(View.GONE);
        mWindowManager.addView(mCircularActionMenu, mWindowLayoutParams);
        mWindowManager.addView(mCircularActionView, mWindowLayoutParams);
    }

    @Override
    public void onServiceDestroy(FloatyService floatyService) {

    }

    @Override
    public void close() {

    }
}
