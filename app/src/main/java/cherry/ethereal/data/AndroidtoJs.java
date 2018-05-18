package cherry.ethereal.data;

import android.content.Intent;
import android.util.Log;
import android.webkit.JavascriptInterface;

import java.util.List;

import cherry.ethereal.EveryDayActivity;

public class AndroidtoJs extends Object {
    private Intent mIntent;
    private EveryDayActivity meveryDayActivity;

    public AndroidtoJs(Intent intent, EveryDayActivity everyDayActivity) {
        mIntent = intent;
        meveryDayActivity = everyDayActivity;
    }

    // 定义JS需要调用的方法
    // 被JS调用的方法必须加入@JavascriptInterface注解
    @JavascriptInterface
    public void play(String ID, String Name, String Author) {
        mIntent.putExtra("songID", ID);
        String[] NameAndAuthor = Name.split("-");
       if(NameAndAuthor.length==2) {
           mIntent.putExtra("songName", NameAndAuthor[0]);
           mIntent.putExtra("songAuthor", NameAndAuthor[1]);
       }else{
           mIntent.putExtra("songName", NameAndAuthor[0]);
           mIntent.putExtra("songAuthor", "未知");
       }


        meveryDayActivity.setResult(0x0002, mIntent);
        meveryDayActivity.finish();
        System.out.println(String.valueOf(ID));
    }
}
