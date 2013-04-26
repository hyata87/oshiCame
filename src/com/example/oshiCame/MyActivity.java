package com.example.oshiCame;

import android.app.Activity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Button;

public class MyActivity extends Activity {

    private SurfaceView surfaceView;
    private Button button;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);


        init();
        setViews();
    }

    private void init() {
    }

    private void setViews() {
        button = (Button)findViewById(R.id.button);
        surfaceView = (SurfaceView)findViewById(R.id.surfaceView);

        //listener登録
        button.setOnTouchListener(new CameraTouchListener());

        SurfaceHolder holder = surfaceView.getHolder();
        holder.addCallback(new CameraPreviewCallback());
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }
}
