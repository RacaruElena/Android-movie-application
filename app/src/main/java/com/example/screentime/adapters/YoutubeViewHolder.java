package com.example.screentime.adapters;

import android.annotation.SuppressLint;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.screentime.R;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;

public class YoutubeViewHolder extends RecyclerView.ViewHolder {
    WebView webView;

    SimpleExoPlayer exoPlayer;
    private PlayerView mExoplayerView;

    //@SuppressLint("SetJavaScriptEnabled")
    public YoutubeViewHolder(@NonNull View itemView) {
        super(itemView);
        webView = itemView.findViewById(R.id.video_view);
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        webView.getSettings().setAllowContentAccess(true);
    }
}
