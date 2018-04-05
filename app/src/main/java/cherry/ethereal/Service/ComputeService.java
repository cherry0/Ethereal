package cherry.ethereal.Service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import cherry.ethereal.Service.Binder.ComputeBinder;

/**
 * Created by many on 2018/4/5.
 */

public class ComputeService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        ComputeBinder computeBinder=new ComputeBinder();
        return computeBinder;
    }
}
