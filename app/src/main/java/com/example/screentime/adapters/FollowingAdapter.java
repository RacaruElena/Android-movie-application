package com.example.screentime.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.screentime.R;
import com.example.screentime.entities.ListModel;
import com.example.screentime.entities.MovieModel;
import com.example.screentime.entities.User;

import java.util.List;

public class FollowingAdapter extends ArrayAdapter<User> {
    private Context context;
    private LayoutInflater inflater;
    private int resource;
    private List<User> users;

    public FollowingAdapter(@NonNull Context context, int resource, @NonNull List<User> objects,
                            LayoutInflater inflater) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.users = objects;
        this.inflater = inflater;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        @SuppressLint("ViewHolder") View view = inflater.inflate(resource, parent, false);
        User user = users.get(position);
        if (user == null) {
            return view;
        }


        addUsername(view, user.getFullName());


        return view;
    }

    private void addUsername(View view, String fullName) {
        TextView textView = view.findViewById(R.id.tv_user_name);
        populateTextViewContent(textView, fullName);
    }

    private void populateTextViewContent(TextView textView, String value) {
        if (value != null && !value.trim().isEmpty()) {
            textView.setText(value);
        } else {
            textView.setText("-");
        }
    }

}
