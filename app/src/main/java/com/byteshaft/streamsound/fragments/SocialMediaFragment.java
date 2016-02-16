package com.byteshaft.streamsound.fragments;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;

import com.byteshaft.streamsound.R;

public class SocialMediaFragment extends Fragment implements View.OnClickListener {

    private View mBaseView;
    private static WebView mWebView;
    private ProgressDialog progressDialog;

    /// urls
    private String facebookUrl = "https://m.facebook.com/codercast";
    private String twitterUrl = "https://m.twitter.com/codercast";
    private String youtubeUrl = "https://m.youtube.com/channel/UCOWJExrlKckmB9lJQGDG3BQ?app=desktop";
    private String instagramUrl = "https://www.instagram.com/codercast";

    // buttons
    private ImageButton buttonFacebook;
    private ImageButton buttonTwitter;
    private ImageButton buttonInstagram;
    private ImageButton buttonYouTube;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.social_media_fragment, container, false);

        buttonFacebook = (ImageButton) mBaseView.findViewById(R.id.fb_button);
        buttonTwitter = (ImageButton) mBaseView.findViewById(R.id.twitter_button);
        buttonInstagram = (ImageButton) mBaseView.findViewById(R.id.instagram_button);
        buttonYouTube = (ImageButton) mBaseView.findViewById(R.id.youtube_button);
        progressDialog = ProgressDialog.show(getActivity(), "", "Loading ...", true);
        /// initializing social media buttons buttons
        buttonFacebook.setOnClickListener(this);
        buttonTwitter.setOnClickListener(this);
        buttonInstagram.setOnClickListener(this);
        buttonYouTube.setOnClickListener(this);

        mWebView = (WebView) mBaseView.findViewById(R.id.webView);
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl(facebookUrl);
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
                    mWebView.loadUrl(facebookUrl);
                break;
            case R.id.twitter_button:
                selected = 1;
                mWebView.loadUrl(twitterUrl);
                break;
            case R.id.youtube_button:
                selected = 2;
                mWebView.loadUrl(youtubeUrl);
                break;
            case R.id.instagram_button:
                selected = 3;
                mWebView.loadUrl(instagramUrl);
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
            progressDialog.dismiss();
            super.onPageFinished(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            progressDialog.show();
            super.onPageStarted(view, url, favicon);
        }
    }
}
