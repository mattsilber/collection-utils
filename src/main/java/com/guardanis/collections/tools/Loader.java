package com.guardanis.collections.tools;

import android.view.View;

public interface Loader {

    public View getView();

    public void update();

    public int getUpdateRate();

    public boolean canUpdate();

    public void start();

    public void stop();

}
