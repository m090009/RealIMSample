package com.m090009.sample.realim.helpers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.util.Log;

/**
 * Created by tohamy on 5/2/15.
 */
public class Utilities {
    public static String FIREBASE_URL = "https://shining-torch-9232.firebaseio.com/android/realim/data";
    public static String AWS_ACCESS_KEY = "AKIAIVD744R4S44RXL5Q";
    public static String AWS_SECERETE_ACCESS_KEY="eS4Ls6MMt6ntxH4z20zXNoiedfEr8SR+fwUbfij9";
    public static String AWS_S3_BUCKET_NAME = "m090009realimimages";
    public static String IMAGE_NAME_PREFIX = "RealIM";
    public static int AWS_S3_IMAGE_URL_DURATION = 360000*144;//One month
    public static String MAIN_IMAGES_FOLDER = "RealIm";


}
