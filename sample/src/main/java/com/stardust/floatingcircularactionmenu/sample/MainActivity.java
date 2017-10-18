package com.stardust.floatingcircularactionmenu.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.stardust.enhancedfloaty.FloatyService;
import com.stardust.floatingcircularactionmenu.CircularActionMenu;
import com.stardust.floatingcircularactionmenu.CircularActionMenuFloatingWindow;
import com.stardust.floatingcircularactionmenu.CircularActionMenuFloaty;
import com.stardust.floatingcircularactionmenu.ShadowWindow;

/**
 * Created by Stardust on 2017/9/25.
 */

public class MainActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final CircularActionMenuFloatingWindow window = new CircularActionMenuFloatingWindow(new CircularActionMenuFloaty() {

            @Override
            public View inflateActionView(FloatyService service, CircularActionMenuFloatingWindow window) {
                return View.inflate(service, R.layout.circular_action_view, null);
            }

            @Override
            public CircularActionMenu inflateMenuItems(FloatyService service, CircularActionMenuFloatingWindow window) {
                return (CircularActionMenu) View.inflate(service, R.layout.circular_action_menu, null);
            }
        });
        startService(new Intent(this, FloatyService.class));
        window.setKeepToSideHiddenWidthRadio(0.5f);
        FloatyService.addWindow(window);

    }
}
