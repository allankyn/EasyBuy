package com.akproject.easybuy.utility;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import java.io.File;

/**
 * Created by Allan on 16/1/2016.
 */
public class FileManager {

    public static void writeSetting(Activity activity, String key, int value) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static int readSetting(Activity activity, String key) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        int defaultValue = 0;   //getResources().getInteger(R.string.saved_high_score_default);
        return sharedPref.getInt(key, defaultValue);
    }

    public static File getStorageFile(String fileName) {
        // Get the directory
        File storageDir = new File(Environment.getExternalStorageDirectory() + File.separator + ConfigManager.STORE_FOLDER);
        if (!storageDir.isDirectory()) {
            if (!storageDir.mkdirs()) {
                //LogManager.logSystem("Can't Create Directory" + storageDir.getAbsolutePath());
                return null;
            } else {
                //LogManager.logSystem(String.format("Create folder %s succeed", storageDir.getAbsolutePath()));
            }
        }

        // Create file
        File file = new File(storageDir, fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                LogManager.logSystem("2" + e.getMessage());
                //LogManager.logSystem("Save error");
                return null;
            }
        } else {
            //LogManager.logSystem("File already exists");
        }
        LogManager.logSystem("3");
        return file;
    }

}
