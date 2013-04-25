package com.example.oshiCame;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.List;

public class MyActivity extends Activity {

    private SurfaceView surfaceView;

    private Camera camera;


    private SurfaceHolder.Callback cameraPreview = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder surfaceHolder) {
            //To change body of implemented methods use File | Settings | File Templates.
            camera = Camera.open();

            try{
                camera.setPreviewDisplay(surfaceHolder);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder surfaceHolder,int format, int width, int height) {
            //To change body of implemented methods use File | Settings | File Templates.
            camera.stopPreview();

            Camera.Parameters parameters = camera.getParameters();

            List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();
            Camera.Size previewSize = previewSizes.get(0);
            parameters.setPreviewSize(previewSize.width, previewSize.height);

            camera.setParameters(parameters);

            camera.startPreview();
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
            //To change body of implemented methods use File | Settings | File Templates.

            if(camera == null){
                return;
            }

            camera.stopPreview();



            camera.release();
            camera = null;
        }
    };

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        setViews();
    }

    private void setViews() {
        surfaceView = (SurfaceView)findViewById(R.id.surfaceView);


        //listener登録
        SurfaceHolder holder = surfaceView.getHolder();
        holder.addCallback(cameraPreview);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }
}
