package com.example.screentime.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.screentime.R;
import com.example.screentime.entities.DataSetList;

import java.util.ArrayList;

public class YoutubeAdapter extends RecyclerView.Adapter<YoutubeViewHolder> {
    private ArrayList<DataSetList> arrayList;
    Context context;

    public YoutubeAdapter(ArrayList<DataSetList> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public YoutubeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.trailer_layout, parent, false);
        return new YoutubeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull YoutubeViewHolder holder, int position) {
        DataSetList current = arrayList.get(position);
        holder.webView.loadUrl(current.getLink());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
