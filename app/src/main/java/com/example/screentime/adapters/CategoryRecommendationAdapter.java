package com.example.screentime.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.screentime.R;
import com.example.screentime.entities.CategoryRecommendation;
import com.example.screentime.entities.User;

import java.util.List;

public class CategoryRecommendationAdapter extends ArrayAdapter<CategoryRecommendation> {

    private Context context;
    private LayoutInflater inflater;
    private int resource;
    private List<CategoryRecommendation> recommendations;

    public CategoryRecommendationAdapter(@NonNull Context context, int resource, @NonNull List<CategoryRecommendation> objects,
                                         LayoutInflater inflater) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.recommendations = objects;
        this.inflater = inflater;
    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        @SuppressLint("ViewHolder") View view = inflater.inflate(resource, parent, false);
        CategoryRecommendation recommendation = recommendations.get(position);
        if (recommendation == null) {
            return view;
        }
        addGenre(view, recommendation.getGenre());
        addNoMovies(view, recommendation.getNoMovie());
        addRatingAverage(view, recommendation.getRatingAverage());

        return view;
    }

    private void addRatingAverage(View view, Double ratingAverage) {
        TextView textView = view.findViewById(R.id.recommendation_rating_average);
        populateTextViewContent(textView, getContext().getString(R.string.rating_average, String.valueOf(String.format("%.2f", ratingAverage))));
    }

    private void addNoMovies(View view, Integer noMovie) {
        TextView textView = view.findViewById(R.id.recommendation_noMovies);
        populateTextViewContent(textView, context.getString(R.string.no_movies, String.valueOf(noMovie)));
    }

    private void addGenre(View view, String genre) {
        TextView textView = view.findViewById(R.id.recommendation_genre);
        populateTextViewContent(textView, genre);
    }

    private void populateTextViewContent(TextView textView, String value) {
        if (value != null && !value.trim().isEmpty()) {
            textView.setText(value);
        } else {
            textView.setText("-");
        }
    }
}
