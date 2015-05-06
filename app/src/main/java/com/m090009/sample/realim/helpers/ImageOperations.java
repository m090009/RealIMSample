package com.m090009.sample.realim.helpers;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.AbstractDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by tohamy on 5/5/15.
 */
public class ImageOperations {
    public static int IMAGE_MESSAGE_WIDTH = 400;
    public static int IMAGE_MESSAGE_HEIGHT = 600;

    public static File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = Utilities.IMAGE_NAME_PREFIX+"_" + timeStamp;
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = null;
        try {
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
        }catch(Exception e){
            e.printStackTrace();
        }
        return image;
    }

    public static File persistImage(Bitmap bitmap,
                                     String name,
                                     Context context) {
        File filesDir = context.getFilesDir();
        File imageFile = new File(filesDir, name);

        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e("Image Operations", "Error writing bitmap", e);
        }
        return imageFile;
    }

    public static String getThumnailPath(Context context, String path) {
        long imageId = -1;

        String[] projection = new String[]{MediaStore.MediaColumns._ID};
        String selection = MediaStore.MediaColumns.DATA + "=?";
        String[] selectionArgs = new String[]{path};
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null);
        if (cursor != null && cursor.moveToFirst()) {
            imageId = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            cursor.close();
        }

        String result = null;
        cursor = MediaStore.Images.Thumbnails.queryMiniThumbnail(context.getContentResolver(),
                imageId, MediaStore.Images.Thumbnails.MINI_KIND, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            result = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Thumbnails.DATA));
            cursor.close();
        }

        return result;
    }

    public static void createAThumbNail(Context context, long selectedImageUri){
        Bitmap bitmap = MediaStore.Images.Thumbnails.getThumbnail(
                context.getContentResolver(),
                selectedImageUri,
                MediaStore.Images.Thumbnails.MINI_KIND,
                null );
    }



    public static void requestImageResize(int width, int height, Uri uri, SimpleDraweeView view){
        ResizeOptions imageResizeOption = new ResizeOptions(width/2, height/2);
        ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(uri)
                .setResizeOptions(imageResizeOption)
                .setLocalThumbnailPreviewsEnabled(true)
                .build();
        AbstractDraweeController controller =
                Fresco.newDraweeControllerBuilder()
                        .setOldController(view.getController())
                        .setImageRequest(imageRequest)
                        .setAutoPlayAnimations(true)
                        .build();
        try {
            view.setController(controller);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
