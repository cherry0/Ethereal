package cherry.ethereal.data;

import android.content.Intent;
import android.webkit.JavascriptInterface;

public class AndroidtoJs extends Object {
    // 定义JS需要调用的方法
    // 被JS调用的方法必须加入@JavascriptInterface注解
    @JavascriptInterface
    public void play(String msg) {
        System.out.println(String.valueOf(msg));
    }
}
