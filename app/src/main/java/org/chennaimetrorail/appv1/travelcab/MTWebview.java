package org.chennaimetrorail.appv1.travelcab;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.webkit.GeolocationPermissions;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.net.URISyntaxException;


/*Created By Abhijeet 23-07-2019*/
public class MTWebview extends WebView {

    private int webViewPreviousState;
    private final int PAGE_STARTED = 0x1;

    public MTWebview(Context context) {
        super(context, null);
    }

    public MTWebview(Context context, AttributeSet attrs) {
        super(context, attrs);
        initWebView();
    }

    private void initWebView() {
        this.getSettings().setJavaScriptEnabled(true);
        this.getSettings().setUseWideViewPort(true);
        this.getSettings().setLoadWithOverviewMode(true);
        this.getSettings().setDomStorageEnabled(true);
        this.setInitialScale(1);
        this.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        this.setScrollbarFadingEnabled(false);

        this.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        this.getSettings().setBuiltInZoomControls(false);
        this.setWebViewClient(new GeoWebViewClient());
        // Below required for geolocation
        this.getSettings().setJavaScriptEnabled(true);
        this.getSettings().setGeolocationEnabled(true);
        this.setWebChromeClient(new GeoWebChromeClient());

       this.getSettings().setAppCacheEnabled(true);
        this.getSettings().setDatabaseEnabled(true);
    }


    public class GeoWebChromeClient extends android.webkit.WebChromeClient {
        @Override
        public void onGeolocationPermissionsShowPrompt(final String origin, final GeolocationPermissions.Callback callback) {
            callback.invoke(origin, true, false);
        }
    }


//-=============================================

    public class GeoWebViewClient extends WebViewClient {
        @Override


        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            System.out.println("shouldOverrideUrlLoading called " + url);


            if (url.startsWith("intent://") || url.startsWith("upi://pay")) {

                try {

                    Context context = view.getContext();

                    Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);


                    if (intent != null) {

                        view.stopLoading();


                        PackageManager packageManager = context.getPackageManager();

                        ResolveInfo info = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);

                        if (info != null) {

                            context.startActivity(intent);

                        } else {

                            String fallbackUrl = intent.getStringExtra("browser_fallback_url");

                            view.loadUrl(fallbackUrl);


                            // or call external broswer

//                                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(fallbackUrl));

//                                    context.startActivity(browserIntent);

                        }


                        return true;

                    }

                } catch (URISyntaxException e) {

                    System.out.println("Can't resolve intent:" + e.getMessage());


                }

            }


            return false;

        }



//        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            view.loadUrl(url);
//
//            return true;
//        }

        Dialog loadingDialog = new Dialog(getContext());

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            webViewPreviousState = PAGE_STARTED;
            if (loadingDialog == null || !loadingDialog.isShowing())
                loadingDialog = ProgressDialog.show(getContext(), "",
                        "Loading...Please wait...", true, true,
                        new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {

                            }
                        });
            loadingDialog.setCancelable(false);
        }


        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
           /* if (isConnected(getContext())) {
                view.loadUrl("javascript:window.location.reload( true )");
            }*/
            super.onReceivedError(view, request, error);

        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, final WebResourceResponse errorResponse) {
               /* if (isConnected(getContext())) {
                    view.loadUrl("javascript:window.location.reload( true )");
                }*/
            super.onReceivedHttpError(view, request, errorResponse);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if (webViewPreviousState == PAGE_STARTED) {
                if (null != loadingDialog) {
                    loadingDialog.dismiss();
                    loadingDialog = null;
                }
            }
        }
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return (activeNetwork != null && activeNetwork.isConnected());
    }
}
