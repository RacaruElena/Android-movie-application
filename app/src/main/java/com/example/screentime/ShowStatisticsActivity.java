package com.example.screentime;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.screentime.entities.MovieModel;
import com.example.screentime.fragments.CompleteListFragment;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ShowStatisticsActivity extends AppCompatActivity {
    private BarChart barChart;
    private ArrayList<BarEntry> barEntryArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_statistics);
        initComponents();

    }

    private void initComponents() {
        barChart = findViewById(R.id.barchart);
        barEntryArrayList = new ArrayList<>();
        Map<String, Integer> source = getSource(CompleteListFragment.movies);

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

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(genres));
        xAxis.setPosition(XAxis.XAxisPosition.TOP_INSIDE);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setLabelCount(genres.size());
        xAxis.setLabelRotationAngle(270);
        barChart.animateY(2000);
        barChart.invalidate();

    }

    private Map<String, Integer> getSource(List<MovieModel> movies) {
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
}