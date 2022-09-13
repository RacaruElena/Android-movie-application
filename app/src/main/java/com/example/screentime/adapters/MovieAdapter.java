package com.example.screentime.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.screentime.R;
import com.example.screentime.entities.MovieModel;

import java.util.List;


public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MyViewHolder> {

    private Context context;
    private List<MovieModel> movies;

    public MovieAdapter(Context context, List<MovieModel> movies) {
        this.context = context;
        this.movies = movies;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        LayoutInflater inflater = LayoutInflater.from(context);
        v = inflater.inflate(R.layout.movie_layout, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tvTitle.setText(movies.get(position).getName());
        holder.tvRating.setText(String.valueOf(movies.get(position).getVoteAverage()));
        Glide.with(context).load("https://image.tmdb.org/t/p/w500" + movies.get(position).getImage()).into(holder.ivMovie);

    }

    @Override
    public int getItemCount() {
        return movies.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        TextView tvRating;
        ImageView ivMovie;
        CardView cardView_movie;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tv_title);
            ivMovie = itemView.findViewById(R.id.imageView_movie);
            tvRating = itemView.findViewById(R.id.tv_rating);
           // cardView_movie=itemView.findViewById(R.id.card_view_movie);
        }
    }


}
