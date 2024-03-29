package com.guardanis.collections.grid.modules;

import android.view.View;

import androidx.core.view.ViewCompat;

import com.guardanis.collections.grid.ModularGridView;

public class HeaderModule extends OuterViewModule {

    /**
     * To use this properly, the GridView must be wrapped in a RelativeLayout and the
     * targetView must be aligned to the top (else it will look weird). This module
     * performs translations to fake the header.
     */
    public HeaderModule(View targetView) {
        super(targetView);
    }

    @Override
    protected void updateParentPadding() {
        final ModularGridView parent = getParent();

        this.originalPadding = originalPadding < 0
            ? parent.getPaddingTop()
            : originalPadding;

        parent.setPadding(
            parent.getPaddingLeft(),
            originalPadding + targetView.getHeight(),
            parent.getPaddingRight(),
            parent.getPaddingBottom()
        );
    }

    @Override
    public void onScroll(int... values) {
        if (isScrollEventProcessable() && values[0] == 0) {
            final View firstView = getParent().getChildAt(0);

            if (firstView != null) {
                translationY = ViewCompat.getY(firstView) - targetView.getHeight() + (originalPadding / 2);
                ViewCompat.setTranslationY(targetView, translationY);
            }
        }
    }

}
