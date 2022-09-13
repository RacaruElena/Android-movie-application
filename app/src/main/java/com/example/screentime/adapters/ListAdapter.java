package com.example.screentime.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.screentime.R;
import com.example.screentime.entities.ListModel;
import com.example.screentime.entities.MovieModel;

import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends ArrayAdapter<ListModel> {
    private Context context;
    private LayoutInflater inflater;
    private int resource;
    private List<ListModel> movieLists;


    public ListAdapter(@NonNull Context context, int resource, @NonNull List<ListModel> objects,
                       LayoutInflater inflater) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.movieLists = objects;
        this.inflater = inflater;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        @SuppressLint("ViewHolder") View view = inflater.inflate(resource, parent, false);
        ListModel list = movieLists.get(position);
        if (list == null) {
            return view;
        }
        TextView tvName=view.findViewById(R.id.tv_list_name);

        LinearLayout linearLayout = view.findViewById(R.id.linear_layout_lists);
        if (position == 0){
            linearLayout.setBackground(context.getResources().getDrawable(R.drawable.the_movies3));
            tvName.setText("Complete List");
        }

        else if (position == 1){
            linearLayout.setBackground(context.getResources().getDrawable(R.drawable.favourite_movies));

        }

        else if (position == 2){
            linearLayout.setBackground(context.getResources().getDrawable(R.drawable.watchlist));

        }



        return view;
    }

}
