package com.example.screentime.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.screentime.PieChartActivity;
import com.example.screentime.R;
import com.example.screentime.ShowStatisticsActivity;
import com.example.screentime.entities.MovieModel;
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

public class StatisticsFragment extends Fragment {
    public static final String CHART_KEY = "CHART_KEY";
    private Button btnBarChart;
    private Button btnPieChart;

    public StatisticsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);
        initComponents(view);
        return view;
    }

    private void initComponents(View view) {
        btnPieChart = view.findViewById(R.id.btn_pie_chart);
        btnPieChart.setOnClickListener(openPieChart());
        btnBarChart = view.findViewById(R.id.btn_bar_chart);
        btnBarChart.setOnClickListener(openBarChart());


    }

    private View.OnClickListener openBarChart() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PieChartActivity.class);
                intent.putExtra(CHART_KEY, 0);
                startActivity(intent);

            }
        };
    }

    private View.OnClickListener openPieChart() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PieChartActivity.class);
                intent.putExtra(CHART_KEY, 1);
                startActivity(intent);
            }
        };
    }


}