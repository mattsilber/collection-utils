package com.guardanis.collections.tools;

public class UpdateLoaderTask implements Runnable {

    private Loader loader;
    private boolean canceled = false;

    public UpdateLoaderTask(Loader loader) {
        this.loader = loader;
    }

    @Override
    public void run() {
        try{
            while(!canceled && loader.canUpdate()){
                loader.getView().post(new Runnable() {
                    public void run() {
                        loader.update();
                    }
                });

                Thread.sleep(loader.getUpdateRate());
            }
        }
        catch(Exception e){ }
    }

    public void cancel(){
        this.canceled = true;
    }
}
