package els.it.services.elsapp;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;


public class MainPageActivity extends Activity{

    private WebView wvMainPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.webkit.CookieManager.setAcceptFileSchemeCookies(true);
        setContentView(R.layout.main_page);
        wvMainPage=(WebView) findViewById(R.id.MainPageWebView);

        WebSettings ws=wvMainPage.getSettings();
        ws.setAllowUniversalAccessFromFileURLs(true);
        ws.setAllowFileAccessFromFileURLs(true);

        wvMainPage.loadUrl("file:///android_asset/www/index.html");
        CookieManager.getInstance().setAcceptCookie(true);

        ws.setJavaScriptEnabled(true);
    }
}
