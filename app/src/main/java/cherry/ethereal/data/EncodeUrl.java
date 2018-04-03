package cherry.ethereal.data;

import android.util.Log;

import java.net.URLEncoder;

/**
 * Created by many on 2018/4/3.
 */

public class EncodeUrl {
    public static String toURLEncoded(String paramString) {
        if (paramString == null || paramString.equals("")) {
            Log.e("toURLEncoded error:", paramString);
            return "";
        }

        try {
            String str = new String(paramString.getBytes(), "UTF-8");
            str = URLEncoder.encode(str, "UTF-8");
            return str;
        } catch (Exception localException) {
            Log.e("toURLEncoded error:", paramString, localException);
        }

        return "";
    }
}
