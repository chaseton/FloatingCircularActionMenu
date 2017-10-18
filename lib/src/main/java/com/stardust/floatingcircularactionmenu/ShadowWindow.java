package com.stardust.floatingcircularactionmenu;

import android.graphics.PixelFormat;
import android.view.View;
import android.view.WindowManager;

import com.stardust.enhancedfloaty.FloatyService;
import com.stardust.enhancedfloaty.FloatyWindow;

/**
 * Created by Stardust on 2017/10/18.
 */

public class ShadowWindow implements FloatyWindow {

    private View mShadowView;
    private View mCloseView;
    private WindowManager.LayoutParams mLayoutParams;
    private WindowManager mWindowManager;
    private View.OnClickListener mOnCloseClickListener;

    @Override
    public void onCreate(FloatyService floatyService, WindowManager windowManager) {
        mWindowManager = windowManager;
        mShadowView = View.inflate(floatyService, R.layout.fcam_shadow, null);
        mCloseView = mShadowView.findViewById(R.id.close);
        mLayoutParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        if (mOnCloseClickListener != null) {
            mCloseView.setOnClickListener(mOnCloseClickListener);
        }
    }

    @Override
    public void onServiceDestroy(FloatyService floatyService) {
        close();
    }

    @Override
    public void close() {
        hide();
        FloatyService.removeWindow(this);
    }

    public void show() {
        mWindowManager.addView(mShadowView, mLayoutParams);
    }

    public void hide() {
        mWindowManager.removeView(mShadowView);
    }

    public void setOnCloseClickListener(View.OnClickListener onClickListener) {
        if (mCloseView == null) {
            mOnCloseClickListener = onClickListener;
            return;
        }
        mCloseView.setOnClickListener(onClickListener);
    }
}
