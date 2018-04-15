package cherry.ethereal;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;

import cherry.ethereal.data.AndroidtoJs;
import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class EveryDayActivity extends SwipeBackActivity {
    private SwipeBackLayout mSwipeBackLayout;
    private WebView mWebView;
    private Button button;
    private ProgressBar mProgressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_every_day);
        mSwipeBackLayout = getSwipeBackLayout();
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);

        mProgressbar = (ProgressBar) findViewById(R.id.progressBar);
        button = (Button) findViewById(R.id.btnReload);
        mWebView = (WebView) findViewById(R.id.webView);
        WebSettings webSettings = mWebView.getSettings();

        // 设置与Js交互的权限
        webSettings.setJavaScriptEnabled(true);
        // 设置允许JS弹窗
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        //开启javascript支持
        webSettings.setUseWideViewPort(true);
        //设置此属性，可任意比例缩放
        webSettings.setLoadWithOverviewMode(true);

        // 通过addJavascriptInterface()将Java对象映射到JS对象
        //参数1：Javascript对象名
        //参数2：Java对象名
        mWebView.addJavascriptInterface(new AndroidtoJs(), "music");//AndroidtoJS类对象映射到js的test对象

        // 加载JS代码
        // 格式规定为:file:///android_asset/文件名.html
        mWebView.loadUrl("http://192.168.1.102/");

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // 当WebView进度改变时更新窗口进
                if (mProgressbar.getVisibility() != View.VISIBLE && newProgress != 100) {
                    mProgressbar.setVisibility(View.VISIBLE);
                }else{
                    mProgressbar.setVisibility(View.GONE);
                }
                mProgressbar.setProgress(newProgress);
                super.onProgressChanged(view, newProgress);
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message,
                                     final JsResult result) {
                AlertDialog.Builder b = new AlertDialog.Builder(EveryDayActivity.this);
                b.setTitle("Alert");
                b.setMessage(message);
                b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.confirm();
                    }
                });
                b.setCancelable(false);
                b.create().show();
                return true;
            }

        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mWebView.reload();
            }
        });
    }
}