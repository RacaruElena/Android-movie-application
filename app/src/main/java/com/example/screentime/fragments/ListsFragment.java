package com.example.screentime.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.screentime.AddMovieActivity;
import com.example.screentime.CompleteListActivity;
import com.example.screentime.FavouriteListActivity;
import com.example.screentime.MovieListActivity;
import com.example.screentime.R;
import com.example.screentime.WatchlistActivity;
import com.example.screentime.adapters.ListAdapter;
import com.example.screentime.adapters.MovieListAdapter;
import com.example.screentime.entities.ListModel;
import com.example.screentime.entities.MovieModel;

import java.util.ArrayList;
import java.util.List;

import android.view.Window;

public class ListsFragment extends Fragment {
    public static final String LIST_KEY = "list_key";
    private ListView lvLists;
    private ArrayList<ListModel> lists;


    public ListsFragment() {
        // Required empty public constructor
    }

//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//    }


    @Override
    public void onResume() {
        super.onResume();
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            activity.getSupportActionBar().hide();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lists, container, false);

        initComponents(view);
        return view;
    }

    private void initComponents(View view) {
        lvLists = view.findViewById(R.id.lv_lists);
        lists = new ArrayList<>();
        ListModel list1 = new ListModel();
        ListModel list2 = new ListModel();
        ListModel list3 = new ListModel();
        lists.add(list1);
        lists.add(list2);
        lists.add(list3);
        addListlvAdapter();
        lvLists.setOnItemClickListener(openCompleteList());

    }

    private AdapterView.OnItemClickListener openCompleteList() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Intent intent = new Intent(getActivity(), CompleteListActivity.class);
                    startActivity(intent);
                } else if (position == 1) {
                    Intent intent = new Intent(getActivity(), FavouriteListActivity.class);
                    startActivity(intent);

                } else if (position == 2) {
                    Intent intent = new Intent(getActivity(), WatchlistActivity.class);
                    startActivity(intent);
                }

            }
        };
    }


    private void addListlvAdapter() {
        ListAdapter adapter = new ListAdapter(getActivity(), R.layout.movies_lists_adapter, lists, getLayoutInflater());
        lvLists.setAdapter(adapter);
    }
}