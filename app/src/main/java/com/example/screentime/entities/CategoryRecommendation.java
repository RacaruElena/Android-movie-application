package com.example.screentime.entities;

public class CategoryRecommendation {
    private String genre;
    private Integer noMovie;
    private Double ratingAverage;

//    public CategoryRecommendation(String genre, Integer noMovie) {
//        this.genre = genre;
//        this.noMovie = noMovie;
//    }


    public CategoryRecommendation(String genre, Integer noMovie, Double ratingAverage) {
        this.genre = genre;
        this.noMovie = noMovie;
        this.ratingAverage = ratingAverage;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Integer getNoMovie() {
        return noMovie;
    }

    public void setNoMovie(Integer noMovie) {
        this.noMovie = noMovie;
    }

    public Double getRatingAverage() {
        return ratingAverage;
    }

    public void setRatingAverage(Double ratingAverage) {
        this.ratingAverage = ratingAverage;
    }
}
