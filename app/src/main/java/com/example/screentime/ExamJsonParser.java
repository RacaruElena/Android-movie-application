package com.example.screentime;

import com.example.screentime.entities.MovieModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ExamJsonParser {


    public static List<MovieModel> fromJson(String json) {
        try {
            JSONObject obj = new JSONObject(json);
            JSONArray array = obj.getJSONArray("results");
            return readExams(array);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    private static List<MovieModel> readExams(JSONArray array) throws JSONException {
        List<MovieModel> results = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            MovieModel movie = readExam(array.getJSONObject(i));
            results.add(movie);
        }
        return results;
    }

    private static MovieModel readExam(JSONObject object) throws JSONException {
        Double vote_average = object.getDouble("vote_average");
        String name = object.getString("title");
        String image = object.getString("poster_path");
        String overview = object.getString("overview");
        String releaseDate = object.getString("release_date");
        String tmdbId = object.getString("id");

        return new MovieModel(vote_average, name, image, overview, releaseDate, tmdbId);
    }

    public static List<String> fromJson2(String json) {
        try {
            JSONObject obj = new JSONObject(json);
            JSONArray array = obj.getJSONArray("results");
            return readMovieIds(array);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    private static List<String> readMovieIds(JSONArray array) throws JSONException {
        List<String> results = new ArrayList<>();
        for (int i = 0; i < array.length() - 1; i++) {
            String movieId = readId(array.getJSONObject(i));
            results.add(movieId);
        }
        return results;
    }

    private static String readId(JSONObject jsonObject) throws JSONException {
        String movieId = jsonObject.getString("id");
        return movieId;

    }

    public static String fromJson3(String json) {
        try {
            JSONObject obj = new JSONObject(json);
            JSONArray array = obj.getJSONArray("results");
            return readMovieVideoKey(array);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new String();
    }

    private static String readMovieVideoKey(JSONArray array) throws JSONException {
        String videoKey = new String();
        videoKey = readVideoKey(array.getJSONObject(array.length() - 1));
        return videoKey;
    }

    private static String readVideoKey(JSONObject jsonObject) throws JSONException {
        String videoKey = jsonObject.getString("key");
        return videoKey;

    }

}
