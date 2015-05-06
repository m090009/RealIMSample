package com.m090009.sample.realim.models;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.ResponseHeaderOverrides;
import com.m090009.sample.realim.helpers.NetworkPreferences;
import com.m090009.sample.realim.helpers.Utilities;
import com.m090009.sample.realim.interfaces.AfterImageTaken;

import java.io.File;
import java.net.URL;
import java.util.Date;

/**
 * Created by tohamy on 5/5/15.
 */
public class AwsS3Impl {
    private AmazonS3Client s3;
    private Boolean ready = false;
    private static String TAG = "Amazon";
    private Context context;
    public AwsS3Impl(Context context){
        this.context = context;
        init(NetworkPreferences.isConnectedToNetwork(context));
    }

    public void init(boolean initialize){
        if(initialize) {
            createS3Client();
            createS3Bucket();
        }
    }

    public void createS3Client(){
        try{
        this.s3 = new AmazonS3Client(
                new BasicAWSCredentials(
                        Utilities.AWS_ACCESS_KEY,
                        Utilities.AWS_SECERETE_ACCESS_KEY));
        } catch (AmazonS3Exception e) {
            e.printStackTrace();
            Log.d(TAG, "Something went wrong while uploading a picture");
        } catch (Exception e){
            e.printStackTrace();
            Log.d(TAG, "Something went wrong while uploading a picture");
        }
    }

    public void createS3Bucket(){
        try {
            new AsyncTask<Void,Void,Void>(){
                @Override
                protected Void doInBackground(Void... params) {
                    s3.createBucket(Utilities.AWS_S3_BUCKET_NAME);
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    setReady(true);
                    Log.d(TAG, "Created New Bucket");
                }
            }.execute();

        } catch (AmazonS3Exception exception){
            exception.printStackTrace();
            Log.d(TAG, "Something went wrong while creating a Bucket");
        }
        setReady(true);
    }


    public void uploadPicture(final File imageFile,
                                final AfterImageTaken callback){
        if(isReady()) {
            try {
                new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... params) {
                        Log.d(TAG, "uploading image: " + imageFile.getName());
                        return putImageInS3Bucket(imageFile);
                    }

                    @Override
                    protected void onPostExecute(String url) {
                        super.onPostExecute(url);
                        callback.afterImageTaken(getPreSignedUrl(imageFile.getName()));
                        Log.d(TAG, "uploaded image: " + imageFile.getName());
                    }
                }.execute();

            } catch (AmazonS3Exception e) {
                e.printStackTrace();
                Log.d(TAG, "Something went wrong while uploading a picture");
            } catch (Exception e) {
                e.printStackTrace();
                Log.d(TAG, "Something went wrong while uploading a picture");
            }
        } else
            Toast.makeText(context, "Amazon services are not ready yet please try again later", Toast.LENGTH_LONG).show();
    }


    public String putImageInS3Bucket(File imageFile){
        String result = null;
        try{
        PutObjectRequest putObjectRequest = new PutObjectRequest(Utilities.AWS_S3_BUCKET_NAME,
                imageFile.getName(),
                imageFile);
        this.s3.putObject(putObjectRequest);
        result = getPreSignedUrl(imageFile.getName());
        } catch (AmazonS3Exception e) {
            e.printStackTrace();
            Log.d(TAG, "Something went wrong while uploading a picture");
        } catch (Exception e){
            e.printStackTrace();
            Log.d(TAG, "Something went wrong while uploading a picture");
        }
        return result;
    }

    public String getPreSignedUrl(String imageName){
        ResponseHeaderOverrides override = new ResponseHeaderOverrides();
        override.setContentType( "image/jpeg" );
        GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest( Utilities.AWS_S3_BUCKET_NAME, imageName );
        urlRequest.setExpiration( new Date( System.currentTimeMillis() + Utilities.AWS_S3_IMAGE_URL_DURATION ) );  // Added an month's worth of milliseconds to the current time.
        urlRequest.setResponseHeaders( override );
        URL url = s3.generatePresignedUrl( urlRequest );
        return url.toString();
    }

    public Boolean isReady() {
        return ready;
    }

    public void setReady(Boolean ready) {
        this.ready = ready;
    }
}
