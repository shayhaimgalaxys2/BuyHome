package il.co.buyhome;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.onesignal.OneSignal;
import com.pushwoosh.Pushwoosh;

import java.net.URISyntaxException;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private static final String UTF_8 = "UTF-8";
    private static final String TEXT_HTML = "text/html";
    private static final String BUY_HOME_URL = "https://www.buy-home.co.il/";

    private String content;
    private ProgressDialog progressDialog;
    private AdvancedWebView webView;
    private boolean issShowingContentPage = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        Pushwoosh.getInstance().registerForPushNotifications();
//        initOneSignal();
        setContentView(R.layout.activity_main);
        webView = findViewById(R.id.webView);
        webView.setBackgroundColor(Color.TRANSPARENT);
        initWebView();
    }

    private void initOneSignal() {
        // Enable verbose OneSignal logging to debug issues if needed.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        // OneSignal Initialization
        OneSignal.initWithContext(this);
        OneSignal.setAppId(App.ONESIGNAL_APP_ID);

        // promptForPushNotifications will show the native Android notification permission prompt.
        // We recommend removing the following code and instead using an In-App Message to prompt for notification permission (See step 7)
        OneSignal.promptForPushNotifications();

    }


    private void initProgressDialog() {
        progressDialog = new ProgressDialog(MainActivity.this, ProgressDialog.THEME_DEVICE_DEFAULT_LIGHT);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private WebViewClient client = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Pattern pattern
                    = Pattern.compile("(^(tel:|mailto:|intent:|sheinlink:|whatsapp:|waze:))|(.pdf$)");

            if (pattern.matcher(url).find()) {
                return openUniversalIntent(url);
            } else {
                view.loadUrl(url);
                return true;
            }
        }

        public void onPageStarted(WebView view, String url,
                                  android.graphics.Bitmap favicon) {
            if (url.equals(BUY_HOME_URL)) {
                initProgressDialog();
            } else if (url.equals("about:blank") && !issShowingContentPage) {
                issShowingContentPage = true;
                webView.loadDataWithBaseURL(null, content, TEXT_HTML, UTF_8,
                        null);
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (url.equals(BUY_HOME_URL) && progressDialog != null) {
                progressDialog.dismiss();
            }
        }
    };

    private boolean openUniversalIntent(String url) {
        try {
            Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException exception) {
                String fallbackUrl = intent.getStringExtra("browser_fallback_url");
                if (fallbackUrl != null) {
                    return openUniversalIntent(fallbackUrl);
                } else {
                    return true;
                }
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return true;
    }

    private void initWebView() {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setBuiltInZoomControls(false);
        settings.setSupportZoom(true);
        settings.setDefaultTextEncodingName(UTF_8);

        webView.loadUrl(BUY_HOME_URL, false);
        webView.setCookiesEnabled(false);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(client);
        webView.setDownloadListener((url, userAgent, contentDisposition, mimetype, contentLength) -> {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        });
    }

    @Override
    public void onBackPressed() {
        if (webView.onBackPressed()) {
            super.onBackPressed();
        }
    }

}
