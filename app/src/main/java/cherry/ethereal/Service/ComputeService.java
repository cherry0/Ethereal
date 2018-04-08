package cherry.ethereal.Service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import cherry.ethereal.Service.Binder.ComputeBinder;

/**
 * Created by many on 2018/4/5.
 */

public class ComputeService extends Service {
    private static final String TAG="Myservice";

    @Override
    public void onCreate()
    {
        Log.v(TAG, "onCreate");
    }

    /**
     * 在整个生命周期里面，只被调用一次
     * @param intent
     * @return
     */
    @Override
    public IBinder onBind(Intent intent) {
        ComputeBinder computeBinder=new ComputeBinder();
        Log.i(TAG,"onBind");
        return computeBinder;
    }

    @Override
    public void onDestroy() {
        Log.i(TAG,"onDestroy");
        super.onDestroy();
    }
    @Override
    public int onStartCommand(Intent intent,int flags,int startID){
        Log.i(TAG,"onStartCommand");
        return super.onStartCommand(intent,flags,startID);
    }

}
