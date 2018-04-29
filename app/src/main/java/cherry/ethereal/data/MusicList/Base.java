package cherry.ethereal.data.MusicList;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Collections;
import java.util.List;


/**
 * Created by many on 2018/4/5.
 */

public class Base {
    protected MusicList BASE;
    protected Activity mActivity;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private final String COLORS_FILE_NAME = "OnlineMusicList.json";

    public Base(MusicList _base, Activity _activity) {
        this.BASE = _base;
        this.mActivity = _activity;
    }

    public Base(Activity _activity) {
        this.mActivity = _activity;
    }


    protected MusicListBase loadMusicList(Activity activity) {
        BufferedReader br=null;
        StringBuilder sb=new StringBuilder();
        MusicListBase musicInfoList = null;
        try {
            br=new BufferedReader(new InputStreamReader(activity.openFileInput("OnlineMusicList.json")));
            String content=br.readLine();
            while (content!=null){
                sb.append(content);
                content=br.readLine();
            }
            Gson gson = new Gson();
            musicInfoList = gson.fromJson(String.valueOf(sb.toString()), new TypeToken<MusicListBase>() {
            }.getType());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return musicInfoList;
    }

    /*
     * 定义文件保存的方法，写入到文件中，所以是输出流
     * */
    protected Boolean saveMusicList(Activity activity, MusicListBase musicListBase) {
        Boolean isSaveFlag = false;
//        verifyStoragePermissions(activity);
        Gson gson = new Gson();
        String content = gson.toJson(musicListBase);
        FileOutputStream fos = null;
        BufferedWriter writer = null;
        try {

            fos = activity.openFileOutput("OnlineMusicList.json", Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(fos));
            writer.write(content);
            writer.flush();
            isSaveFlag = true;
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                if (writer != null) {
                    writer.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return isSaveFlag;
    }

    /**
     * Checks if the app has permission to write to device storage
     * <p>
     * If the app does not has permission then the user will be prompted to
     * grant permissions
     *
     * @param activity
     */
    /**
     * Checks if the app has permission to write to device storage
     * <p>
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
}
