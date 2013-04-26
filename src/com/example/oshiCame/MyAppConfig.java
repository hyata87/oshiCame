package com.example.oshiCame;

import android.hardware.Camera;

public class MyAppConfig {
    private static MyAppConfig ourInstance = new MyAppConfig();


    public static final String SAVE_DIR = "/oscm";

    private Integer pressValue;
    private Camera camera = null;

    public static MyAppConfig getInstance() {
        return ourInstance;
    }

    private MyAppConfig() {
    }

    public Integer getPressValue() {
        return pressValue;
    }

    public void setPressValue(Integer pressValue) {
        this.pressValue = pressValue;
    }

    public Camera getCamera() {
        if(camera == null){
            camera = Camera.open();
        }
        return camera;
    }
}
