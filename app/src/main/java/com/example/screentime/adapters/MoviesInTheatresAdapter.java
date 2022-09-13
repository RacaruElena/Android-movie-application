package com.example.screentime.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.screentime.R;
import com.example.screentime.entities.MovieModel;

import java.util.List;

public class MoviesInTheatresAdapter extends RecyclerView.Adapter<MoviesInTheatresAdapter.MyViewHolder> {
    private Context context;
    private List<MovieModel> exams;

    public MoviesInTheatresAdapter(Context context, List<MovieModel> exams) {
        this.context = context;
        this.exams = exams;
    }

    public MoviesInTheatresAdapter(Context context, List<MovieModel> exams, RecyclerViewClickListener listener) {
        this.context = context;
        this.exams = exams;
    }

    @NonNull
    @Override
    public MoviesInTheatresAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        LayoutInflater inflater = LayoutInflater.from(context);
        v = inflater.inflate(R.layout.movie_in_theatre, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesInTheatresAdapter.MyViewHolder holder, int position) {
        holder.tvTitle.setText(exams.get(position).getName());
        Glide.with(context).load("https://image.tmdb.org/t/p/w500" + exams.get(position).getImage()).into(holder.ivMovie);

    }

    @Override
    public int getItemCount() {
        return exams.size();
    }

    public interface RecyclerViewClickListener {
        void onClick(View v, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvTitle;
        TextView tvRating;
        ImageView ivMovie;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tv_title);

            ivMovie = itemView.findViewById(R.id.imageView_movie);


        }


    }
}
