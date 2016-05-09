package com.example.administrator.myapplication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.File;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.util.EncodingUtils;

public class MainActivity extends Activity  {
    private WebView mWebView;


    // -----
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String APP_CACHE_DIRNAME = "/webcache"; // web缓存目录
    private String url; // 网页url
    private int page = 1;
    private int index = 0;
    private int id = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWebView = (WebView) this.findViewById(R.id.my_webview);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        // webView.loadUrl("http://www.baidu.com");

        WebSettings settings = mWebView.getSettings();
        // 设置javaScript可用
        settings.setJavaScriptEnabled(true);
        // 绑定javaScript接口，可以实现在javaScript中调用我们的Android代码
        // webView.addJavascriptInterface(new WebAppInterface(this), "Android");
        // webView.setWebViewClient(new MyWebViewClient());

        // 加载assets目录下的html页面
        // mWebView.loadUrl("file:///android_asset/01.html");
//        url = "http://192.168.198.130:8019/guide/downloadGuidemark?page=1&id=28";


        findView();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    public void findView() {
        initWebView();

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onLoadResource(WebView view, String url) {
                Log.i(TAG, "onLoadResource url=" + url);

                super.onLoadResource(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.i(TAG, "intercept url=" + url);
                view.loadUrl(url);
                return true;
            }

            // 页面开始时调用
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Log.e(TAG, "onPageStarted");

                super.onPageStarted(view, url, favicon);
            }

            // 页面加载完成调用
            @Override
            public void onPageFinished(WebView view, String url) {

                Log.e(TAG, "onPageFinished" + index);

                index = index + 1;

                if (page >= index) {
                    String s = "http://qa.seeyouabroad.org:8080/guide/downloadGuidemark?page=" + index + "&id=" + id;
                    System.out.println(s);
                    mWebView.loadUrl(s);
                }
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG)
                        .show();
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message,
                                     JsResult result) {
                Log.e(TAG, "onJsAlert " + message);

                Toast.makeText(getApplicationContext(), message,
                        Toast.LENGTH_SHORT).show();
                result.confirm();
                return super.onJsAlert(view, url, message, result);
            }

            @Override
            public boolean onJsConfirm(WebView view, String url,
                                       String message, JsResult result) {
                Log.e(TAG, "onJsConfirm " + message);
                return super.onJsConfirm(view, url, message, result);
            }

            @Override
            public boolean onJsPrompt(WebView view, String url, String message,
                                      String defaultValue, JsPromptResult result) {
                Log.e(TAG, "onJsPrompt " + url);
                return super.onJsPrompt(view, url, message, defaultValue,
                        result);
            }
        });

       //需要访问的网址
        mWebView.loadUrl("http://192.168.198.130:8019/guide/downloadGuidemark?page=1&id=28");
    }

    public void initWebView() {
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setRenderPriority(RenderPriority.HIGH);
        // 建议缓存策略为，判断是否有网络，有的话，使用LOAD_DEFAULT,无网络时，使用LOAD_CACHE_ELSE_NETWORK   LOAD_CACHE_ONLY

        mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); // 设置缓存模式
        // 开启DOM storage API 功能
        mWebView.getSettings().setDomStorageEnabled(true);
        // 开启database storage API功能
        mWebView.getSettings().setDatabaseEnabled(true);

//        String cacheDirPath =  Environment.getExternalStorageDirectory()+"change1";
//                + APP_CACHE_DIRNAME;

        String cacheDirPath = getFilesDir().getAbsolutePath() + APP_CACHE_DIRNAME;
        Log.i("cachePath", cacheDirPath);
        // 设置数据库缓存路径
        mWebView.getSettings().setDatabasePath(cacheDirPath); // API 19 deprecated
        // 设置Application caches缓存目录
        mWebView.getSettings().setAppCachePath(cacheDirPath);
        // 开启Application Cache功能
        mWebView.getSettings().setAppCacheEnabled(true);

        Log.i("databasepath", mWebView.getSettings().getDatabasePath());

    }

    public void clearWebViewCache() {
        // 清理WebView缓存数据库
        try {
            deleteDatabase("webview.db");
            deleteDatabase("webviewCache.db");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // WebView缓存文件
        File appCacheDir = new File(getFilesDir().getAbsolutePath() + APP_CACHE_DIRNAME);
        Log.e(TAG, "appCacheDir path=" + appCacheDir.getAbsolutePath());

        File webviewCacheDir = new File(getCacheDir().getAbsolutePath() + "/webviewCache");
//                + "/webviewCache");
        Log.e(TAG, "appCacheDir path=" + webviewCacheDir.getAbsolutePath());

        // 删除webView缓存目录
        if (webviewCacheDir.exists()) {

            deleteFile(webviewCacheDir);
        }
        // 删除webView缓存，缓存目录
        if (appCacheDir.exists()) {
            deleteFile(appCacheDir);
        }
    }

    public void deleteFile(File file) {
        Log.i(TAG, "delete file path=" + file.getAbsolutePath());
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteFile(files[i]);
                }
            }
            file.delete();
        } else {
            Log.e(TAG, "delete file no exists " + file.getAbsolutePath());
        }
    }



    /**
     * 用于控制页面导航
     *
     * @author wwj_748
     */
    private class MyWebViewClient extends WebViewClient {
        /**
         * 当用于点击链接，系统调用这个方法
         */
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (Uri.parse(url).getHost().equals("www.baidu.com")) {
                // 这个是我的网页，所以不要覆盖，让我的WebView来加载页面
                return false;
            }
            // 否则，这个链接不是我的网站页面，因此启用浏览器来处理urls
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
            return true;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 检查是否为返回事件，如果有网页历史记录
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        // 如果不是返回键或没有网页浏览历史，保持默认
        // 系统行为（可能会退出该活动）
        return super.onKeyDown(keyCode, event);
    }


}