package com.guardanis.collections.grid.modules;

import android.graphics.Canvas;
import android.support.v4.view.ViewCompat;
import android.view.MotionEvent;
import android.view.View;

import com.guardanis.collections.CollectionModule;
import com.guardanis.collections.grid.ModularGridView;
import com.guardanis.collections.list.ModularListView;

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
        this.originalPadding = parent.getPaddingBottom();

        parent.setPadding(parent.getPaddingLeft(),
                parent.getPaddingTop(),
                parent.getPaddingRight(),
                originalPadding + targetView.getHeight());
    }

    @Override
    public void onScroll(int... values) {
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