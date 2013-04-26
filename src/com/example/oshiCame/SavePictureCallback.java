package com.example.oshiCame;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.hardware.Camera;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created with IntelliJ IDEA.
 * User: hyata
 * Date: 13/04/26
 * Time: 15:16
 */
public class SavePictureCallback implements Camera.PictureCallback {

    private  Activity _activity;


    public SavePictureCallback(Activity activity){
        _activity = activity;
    }

    @Override
    public void onPictureTaken(byte[] bytes, Camera camera) {
        //To change body of implemented methods use File | Settings | File Templates.
        if(bytes == null){
            return;
        }

        String saveDir = Environment.getExternalStorageDirectory().getPath() + MyAppConfig.SAVE_DIR;

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

    private void registAndroidDB(String imgPath) {
        ContentValues values = new ContentValues();
        ContentResolver contentResolver = _activity.getContentResolver();
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put("_data", imgPath);
        contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }
}
