package com.guardanis.collections.tools;

import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.View;

import java.lang.ref.WeakReference;

public class UpdateLoaderTask implements Runnable {

    private final WeakReference<View> view;
    private final Runnable onUpdateCallback;

    private long updateRateMs = 50;
    private boolean canceled = false;

    public UpdateLoaderTask(View view, Runnable onUpdateCallback) {
        this.view = new WeakReference<View>(view);
        this.onUpdateCallback = onUpdateCallback;
    }

    public UpdateLoaderTask setUpdateRateMs(long updateRateMs){
        this.updateRateMs = updateRateMs;
        return this;
    }

    @Override
    public void run() {
        try{
            while(isProcessingEnabled()){
                view.get()
                        .post(onUpdateCallback);

                Thread.sleep(updateRateMs);
            }
        }
        catch(Exception e){ e.printStackTrace(); }
    }

    protected boolean isProcessingEnabled(){
        View toManage = view.get();

        return !canceled
                && (toManage != null && ViewCompat.isAttachedToWindow(toManage));
    }

    public void cancel(){
        this.canceled = true;
    }
}
