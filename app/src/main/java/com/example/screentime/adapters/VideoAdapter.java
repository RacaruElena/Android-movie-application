package com.example.screentime.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.screentime.R;
import com.example.screentime.entities.DataSetList;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoHolder> {
    List<DataSetList> youtubeVideoList;

    public VideoAdapter() {
    }

    @NonNull
    @Override
    public VideoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_layout, parent, false);
        return new VideoHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoHolder holder, int position) {
        holder.webView.loadData(youtubeVideoList.get(position).getLink(), "text/html", "utf-8");
    }

    @Override
    public int getItemCount() {
        return youtubeVideoList.size();
    }

    public VideoAdapter(List<DataSetList> youtubeVideoList) {
        this.youtubeVideoList = youtubeVideoList;
    }

    public class VideoHolder extends RecyclerView.ViewHolder {
        WebView webView;

        public VideoHolder(@NonNull View itemView) {
            super(itemView);
            webView = itemView.findViewById(R.id.video_view);
            webView.setWebChromeClient(new WebChromeClient());
            webView.getSettings().setJavaScriptEnabled(true);

        }
    }
}
