package com.guardanis.collections.tools;

import android.annotation.SuppressLint;
import android.view.View;

public class ViewHelper {

    @SuppressLint("NewApi")
    public static void disableHardwareAcceleration(View v) {
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB)
            v.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

}
