package cherry.ethereal.data;

import android.content.Intent;
import android.webkit.JavascriptInterface;

import cherry.ethereal.EveryDayActivity;

public class AndroidtoJs extends Object {
    private  Intent mIntent;
    private  EveryDayActivity meveryDayActivity;
    public AndroidtoJs(Intent intent, EveryDayActivity everyDayActivity){
        mIntent=intent;
        meveryDayActivity=everyDayActivity;
    }
    // 定义JS需要调用的方法
    // 被JS调用的方法必须加入@JavascriptInterface注解
    @JavascriptInterface
    public void play(String msg) {
        mIntent.putExtra("songID",msg);
        meveryDayActivity.setResult(0x0002,mIntent);
        meveryDayActivity.finish();
        System.out.println(String.valueOf(msg));
    }
}
