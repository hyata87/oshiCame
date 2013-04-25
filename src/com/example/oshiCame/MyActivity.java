package com.example.oshiCame;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MyActivity extends Activity {

    private SurfaceView surfaceView;

    private Camera camera;
    private Button button;
    private Timer timer = null;
    private Integer num = 0;


    private SurfaceHolder.Callback cameraPreview = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder surfaceHolder) {
            //To change body of implemented methods use File | Settings | File Templates.
            camera = Camera.open();
            camera.setDisplayOrientation(90);
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


        init();
        setViews();
    }

    private void init() {
        //To change body of created methods use File | Settings | File Templates.
        num = 0;
        timer = null;
    }

    private void setViews() {
        button = (Button)findViewById(R.id.button);
        surfaceView = (SurfaceView)findViewById(R.id.surfaceView);

        //listener登録
        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        timer = new Timer(true);

                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                //To change body of implemented methods use File | Settings | File Templates.
                                num++;
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

                        savePictrue();

                        num = 0;

                        break;
                }
                return false;  //To change body of implemented methods use File | Settings | File Templates.
            }
        });

        SurfaceHolder holder = surfaceView.getHolder();
        holder.addCallback(cameraPreview);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    private void savePictrue() {

        camera.takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] bytes, Camera camera) {
                //To change body of implemented methods use File | Settings | File Templates.
                if(bytes == null){
                    return;
                }

                String saveDir = Environment.getExternalStorageDirectory().getPath() + "/test";

                File file = new File(saveDir);

                if (!file.exists()) {
                    if (!file.mkdir()) {
                        Log.e("Debug", "Make Dir Error");
                    }
                }

                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd_HHmmss");
                String imgPath = saveDir + "/" + sf.format(cal.getTime()) + ".jpg";

                FileOutputStream fos;
                try {
                    fos = new FileOutputStream(imgPath, true);
                    fos.write(bytes);
                    fos.close();

                    // アンドロイドのデータベースへ登録
                    // (登録しないとギャラリーなどにすぐに反映されないため)
                    registAndroidDB(imgPath);

                } catch (Exception e) {
                    Log.e("Debug", e.getMessage());
                }

                // takePicture するとプレビューが停止するので、再度プレビュースタート
                camera.startPreview();
            }
        });
    }

    private void registAndroidDB(String imgPath) {
        //To change body of created methods use File | Settings | File Templates.
        ContentValues values = new ContentValues();
        ContentResolver contentResolver = this.getContentResolver();
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put("_data", imgPath);
        contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }
}
