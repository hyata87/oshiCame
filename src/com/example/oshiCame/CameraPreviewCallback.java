package com.example.oshiCame;

import android.hardware.Camera;
import android.view.SurfaceHolder;

import java.util.List;

public class CameraPreviewCallback implements SurfaceHolder.Callback {

    private Camera _camera;

    public  CameraPreviewCallback(){
        _camera = MyAppConfig.getInstance().getCamera();
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        //To change body of implemented methods use File | Settings | File Templates.
        _camera.setDisplayOrientation(90);
        try{
            _camera.setPreviewDisplay(surfaceHolder);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
        //To change body of implemented methods use File | Settings | File Templates.

        _camera.stopPreview();

        Camera.Parameters parameters = _camera.getParameters();

        List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();
        Camera.Size previewSize = previewSizes.get(0);
        parameters.setPreviewSize(previewSize.width, previewSize.height);

        _camera.setParameters(parameters);

        _camera.startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        //To change body of implemented methods use File | Settings | File Templates.
        if(_camera == null){
            return;
        }
        _camera.stopPreview();
        _camera.release();
        _camera = null;
    }
}
