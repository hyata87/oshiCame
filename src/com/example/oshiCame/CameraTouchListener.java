package com.example.oshiCame;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

public class CameraTouchListener implements View.OnTouchListener {

    protected MyAppConfig config = MyAppConfig.getInstance();

    private  Timer timer;

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        switch (motionEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                timer = new Timer(true);
                config.setPressValue(0);
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        //To change body of implemented methods use File | Settings | File Templates.
                        config.setPressValue(config.getPressValue() + 1);
                    }
                }, 0 ,100);


                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:

                if(timer == null){
                    break;
                }

                timer.cancel();
                timer = null;

                savePictrue((Activity)view.getContext());

                break;
        }

        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    private void savePictrue(Activity activity) {
        config.getCamera().takePicture(null, null, new SavePictureCallback(activity));
    }
}
