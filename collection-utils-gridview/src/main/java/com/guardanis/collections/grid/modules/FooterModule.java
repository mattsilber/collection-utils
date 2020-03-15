package com.guardanis.collections.grid.modules;

import android.view.View;

import com.guardanis.collections.grid.ModularGridView;

import androidx.core.view.ViewCompat;

public class FooterModule extends OuterViewModule {

    /**
     * To use this properly, the GridView must be wrapped in a RelativeLayout and the
     * targetView must be aligned to the bottom (else it will look weird). This module
     * performs translations to fake the footer.
     */
    public FooterModule(View targetView){
        super(targetView);
    }

    @Override
    protected void updateParentPadding() {
        final ModularGridView parent = getParent();

        this.originalPadding = originalPadding < 0
                ? parent.getPaddingBottom()
                : originalPadding;

        parent.setPadding(parent.getPaddingLeft(),
                parent.getPaddingTop(),
                parent.getPaddingRight(),
                originalPadding + targetView.getHeight());
    }

    @Override
    public void onScroll(int... values) {
        final ModularGridView parent = getParent();

        if(isScrollEventProcessable()){
            if (values[0] + values[1] >= values[2]) {
                final View lastView = parent.getChildAt(parent.getChildCount() - 1);

                if (lastView != null) {
                    translationY = targetView.getHeight() - (parent.getHeight() - (ViewCompat.getY(lastView) + lastView.getHeight())) - (originalPadding / 2);
                    ViewCompat.setTranslationY(targetView, translationY);
                }
            }
            else {
                translationY = targetView.getHeight();
                ViewCompat.setTranslationY(targetView, translationY);
            }
        }
    }

}
