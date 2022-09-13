package com.example.screentime;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.screentime.entities.MovieModel;
import com.example.screentime.fragments.CompleteListFragment;
import com.example.screentime.fragments.FollowingFragment;
import com.example.screentime.fragments.StatisticsFragment;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PieChartActivity extends AppCompatActivity {
    private PieChart pieChart;
    private ArrayList<PieEntry> pieEntryArrayList;
    private ArrayList<BarEntry> barEntryArrayList;
    private BarChart barChart;
    private Intent intent;
    private int chart_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart);
        intent = getIntent();
        if (intent != null) {
            chart_type = intent.getIntExtra(StatisticsFragment.CHART_KEY, 0);

        }
        initComponents();

    }

    private void initComponents() {
        pieChart = findViewById(R.id.pie_chart);
        barChart = findViewById(R.id.bar_chart);
        showMoviesByGenreChart();
        if (chart_type == 1) {
            barChart.setVisibility(View.INVISIBLE);
            pieChart.setVisibility(View.VISIBLE);
            showMoviesByRatingChart();
        } else {
            barChart.setVisibility(View.VISIBLE);
            pieChart.setVisibility(View.INVISIBLE);
            showMoviesByGenreChart();
        }
    }

    private AdapterView.OnItemSelectedListener loadStatistics() {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (parent.getItemAtPosition(position).toString()) {
                    case "Show movies by genre":
                        showMoviesByGenreChart();
                        break;
                    case "Show movies by rating":
                        showMoviesByRatingChart();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    }


    private void showMoviesByGenreChart() {
        barEntryArrayList = new ArrayList<>();
        Map<String, Integer> source = getSourceBarChart(CompleteListFragment.movies);

        List<Integer> noMovies = new ArrayList(source.values());
        List<String> genres = new ArrayList(source.keySet());
        for (int i = 0; i < noMovies.size(); i++) {
            barEntryArrayList.add(new BarEntry(i, noMovies.get(i)));
        }
        BarDataSet barDataSet = new BarDataSet(barEntryArrayList, "Movies by genre");
        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);

        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        barChart.getDescription().setEnabled(false);
        barChart.getLegend().setEnabled(false);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(genres));
        xAxis.setTextColor(Color.BLACK);
        xAxis.setTextSize(15);
        xAxis.setPosition(XAxis.XAxisPosition.TOP);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setLabelCount(genres.size());
        xAxis.setLabelRotationAngle(270);
        barChart.animateY(2000);
        barChart.invalidate();
    }

    private void showMoviesByRatingChart() {
        pieEntryArrayList = new ArrayList<>();
        Map<String, Integer> source = getSourcePieChart(CompleteListFragment.movies);
        List<Integer> noMovies = new ArrayList(source.values());
        List<String> myRating = new ArrayList(source.keySet());
        for (int i = 0; i < noMovies.size(); i++) {
            pieEntryArrayList.add(new PieEntry(noMovies.get(i), myRating.get(i)));
        }
        PieDataSet pieDataSet = new PieDataSet(pieEntryArrayList, "Rating");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(16f);
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterText("Movies by rating");
        pieChart.animateY(1400, Easing.EaseInOutQuad);
    }

    private Map<String, Integer> getSourcePieChart(List<MovieModel> movies) {
        if (movies == null || movies.isEmpty()) {
            return new HashMap<>();
        }
        Map<String, Integer> source = new HashMap<>();
        for (MovieModel movie : movies) {
            if (source.containsKey(movie.getMyRating())) {
                Integer currentValue = source.get(movie.getMyRating());
                source.put(movie.getMyRating(), currentValue + 1);
            } else {
                source.put(movie.getMyRating(), 1);
            }
        }
        return source;
    }

    private Map<String, Integer> getSourceBarChart(List<MovieModel> movies) {
        if (movies == null || movies.isEmpty()) {
            return new HashMap<>();
        }
        Map<String, Integer> source = new HashMap<>();
        for (MovieModel movie : movies) {
            if (source.containsKey(movie.getGenre().toString())) {
                Integer currentValue = source.get(movie.getGenre().toString());
                source.put(movie.getGenre().toString(), currentValue + 1);
            } else {
                source.put(movie.getGenre().toString(), 1);
            }
        }
        return source;
    }


}