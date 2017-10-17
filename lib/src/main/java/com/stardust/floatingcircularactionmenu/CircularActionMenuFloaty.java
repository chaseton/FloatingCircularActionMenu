package com.stardust.floatingcircularactionmenu;

import android.view.View;

import com.stardust.enhancedfloaty.FloatyService;

/**
 * Created by Stardust on 2017/9/25.
 */

public interface CircularActionMenuFloaty {

    View inflateActionView(FloatyService service, CircularActionMenuFloatingWindow window);

    CircularActionMenu inflateMenuItems(FloatyService service, CircularActionMenuFloatingWindow window);


}
