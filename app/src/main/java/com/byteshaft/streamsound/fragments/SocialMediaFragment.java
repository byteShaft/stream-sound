package com.byteshaft.streamsound.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.byteshaft.streamsound.R;
import com.byteshaft.streamsound.utils.AppGlobals;

public class SocialMediaFragment extends Fragment implements View.OnClickListener {

    private View mBaseView;
    private static WebView mWebView;
    ProgressBar progressBar;

    // buttons
    private ImageButton buttonFacebook;
    private ImageButton buttonTwitter;
    private ImageButton buttonInstagram;
    private ImageButton buttonYouTube;

    private static SocialMediaFragment sInstance;

    public static SocialMediaFragment getInstance() {
        return sInstance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.social_media_fragment, container, false);
        sInstance = this;

        buttonFacebook = (ImageButton) mBaseView.findViewById(R.id.fb_button);
        buttonTwitter = (ImageButton) mBaseView.findViewById(R.id.twitter_button);
        buttonInstagram = (ImageButton) mBaseView.findViewById(R.id.instagram_button);
        buttonYouTube = (ImageButton) mBaseView.findViewById(R.id.youtube_button);
        progressBar = (ProgressBar) mBaseView.findViewById(R.id.progressBar);

        /// initializing social media buttons buttons
        buttonFacebook.setOnClickListener(this);
        buttonTwitter.setOnClickListener(this);
        buttonInstagram.setOnClickListener(this);
        buttonYouTube.setOnClickListener(this);

        mWebView = (WebView) mBaseView.findViewById(R.id.webView);
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.loadUrl(AppGlobals.facebookUrl);
        buttonFacebook.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        System.out.println("Social");
        return mBaseView;
    }

    public static boolean canGoBack(){
        return mWebView.canGoBack();
    }

    public static void goBack(){
        mWebView.goBack();
    }

    @Override
    public void onClick(View v) {
        int selected = 0;
        switch (v.getId()) {
            case R.id.fb_button:
                selected = 0;
                    mWebView.loadUrl(AppGlobals.facebookUrl);
                break;
            case R.id.twitter_button:
                selected = 1;
                mWebView.loadUrl(AppGlobals.twitterUrl);
                break;
            case R.id.youtube_button:
                selected = 2;
                mWebView.loadUrl(AppGlobals.youtubeUrl);
                break;
            case R.id.instagram_button:
                selected = 3;
                mWebView.loadUrl(AppGlobals.instagramUrl);
                break;

        }
        setSelected(selected);
    }

    private void setSelected(int value) {
        resetColor(buttonFacebook);
        resetColor(buttonInstagram);
        resetColor(buttonTwitter);
        resetColor(buttonYouTube);
        switch (value) {
            case 0:
                buttonFacebook.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                break;
            case 1:
                buttonTwitter.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                break;
            case 2:
                buttonYouTube.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                break;
            case 3:
                buttonInstagram.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                break;
        }
    }

    private void resetColor(ImageButton button) {
        button.setColorFilter(ContextCompat.getColor(getContext(), R.color.transparent));
    }

    private class MyWebViewClient extends WebViewClient {

        @Override
        public void onPageFinished(WebView view, String url) {
            progressBar.setVisibility(View.GONE);
            mWebView.setVisibility(View.VISIBLE);
            super.onPageFinished(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            progressBar.setVisibility(View.VISIBLE);
            mWebView.setVisibility(View.GONE);
            super.onPageStarted(view, url, favicon);
        }
    }
}
