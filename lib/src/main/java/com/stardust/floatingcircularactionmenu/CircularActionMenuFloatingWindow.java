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
    private CircularActionMenu mCircularActionMenu;
    private View mCircularActionView;
    private BounceDragGesture mDragGesture;
    private WindowBridge mActionViewWindowBridge;
    private WindowBridge mMenuWindowBridge;
    private WindowManager.LayoutParams mActionViewWindowLayoutParams;
    private WindowManager.LayoutParams mMenuWindowLayoutParams;
    private View.OnClickListener mActionViewOnClickListener;
    private float mKeepToSideHiddenWidthRadio;


    public CircularActionMenuFloatingWindow(CircularActionMenuFloaty floaty) {
        mFloaty = floaty;
    }

    @Override
    public void onCreate(FloatyService service, WindowManager manager) {
        mWindowManager = manager;
        mActionViewWindowLayoutParams = createWindowLayoutParams();
        mMenuWindowLayoutParams = createWindowLayoutParams();
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
        mDragGesture = new BounceDragGesture(mActionViewWindowBridge, mCircularActionView);
        mDragGesture.setKeepToSideHiddenWidthRadio(mKeepToSideHiddenWidthRadio);
    }

    private void initWindowBridge() {
        mActionViewWindowBridge = new WindowBridge.DefaultImpl(mActionViewWindowLayoutParams, mWindowManager, mCircularActionView);
        mMenuWindowBridge = new WindowBridge.DefaultImpl(mMenuWindowLayoutParams, mWindowManager, mCircularActionMenu);
    }

    public void setKeepToSideHiddenWidthRadio(float keepToSideHiddenWidthRadio) {
        mKeepToSideHiddenWidthRadio = keepToSideHiddenWidthRadio;
        if (mDragGesture != null)
            mDragGesture.setKeepToSideHiddenWidthRadio(mKeepToSideHiddenWidthRadio);
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
        setOnActionViewClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isExpanded()) {
                    collapse();
                } else {
                    expand();
                }
            }
        });
        if (mActionViewOnClickListener != null) {
            mDragGesture.setOnDraggedViewClickListener(mActionViewOnClickListener);
        }
    }

    public void setOnActionViewClickListener(View.OnClickListener listener) {
        if (mDragGesture == null) {
            mActionViewOnClickListener = listener;
            return;
        }
        mDragGesture.setOnDraggedViewClickListener(listener);
    }

    public void expand() {
        mDragGesture.setEnabled(false);
        setMenuPositionAtActionView();
        if (mActionViewWindowBridge.getX() > mActionViewWindowBridge.getScreenWidth() / 2) {
            mCircularActionMenu.expand(Gravity.LEFT);
        } else {
            mCircularActionMenu.expand(Gravity.RIGHT);
        }
    }

    public void collapse() {
        mDragGesture.setEnabled(true);
        setMenuPositionAtActionView();
        mCircularActionMenu.collapse();
    }


    public boolean isExpanded() {
        return mCircularActionMenu.isExpanded();
    }

    private void initWindowView(FloatyService service) {

    }

    private void setMenuPositionAtActionView() {
        int y = mActionViewWindowBridge.getY() - mCircularActionMenu.getMeasuredHeight() / 2 + mCircularActionView.getMeasuredHeight() / 2;
        int x;
        if (mActionViewWindowBridge.getX() > mActionViewWindowBridge.getScreenWidth() / 2) {
            x = mActionViewWindowBridge.getX() - mCircularActionMenu.getExpandedWidth() + mCircularActionView.getMeasuredWidth() / 2;
        } else {
            x = mActionViewWindowBridge.getX() - mCircularActionMenu.getExpandedWidth() + mCircularActionView.getMeasuredWidth();
        }
        mMenuWindowBridge.updatePosition(x, y);
    }

    private void inflateWindowViews(FloatyService service) {
        mCircularActionMenu = mFloaty.inflateMenuItems(service, this);
        mCircularActionView = mFloaty.inflateActionView(service, this);
        mCircularActionMenu.setVisibility(View.GONE);
        mWindowManager.addView(mCircularActionMenu, mActionViewWindowLayoutParams);
        mWindowManager.addView(mCircularActionView, mMenuWindowLayoutParams);
    }

    @Override
    public void onServiceDestroy(FloatyService floatyService) {
        close();
    }

    @Override
    public void close() {
        mWindowManager.removeView(mCircularActionMenu);
        mWindowManager.removeView(mCircularActionView);
        FloatyService.removeWindow(this);
    }
}
