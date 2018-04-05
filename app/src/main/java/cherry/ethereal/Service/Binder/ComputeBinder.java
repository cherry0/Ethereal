package cherry.ethereal.Service.Binder;

import android.os.Binder;

/**
 * Created by many on 2018/4/5.
 */

public class ComputeBinder extends Binder {
    public String count(String name) {
        return name;
    }
}
