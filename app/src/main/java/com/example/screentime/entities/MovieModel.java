package com.example.screentime.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class MovieModel implements Parcelable {
    private String id;
    private double voteAverage;
    private String name;
    private String image;
    private String overview;
    private String releaseDate;
    private boolean favourite;
    private String notes;
    private Genre genre;
    private String myRating;
    private int ratingValue;
    private String tmdbId;

    public String getMyRating() {
        return myRating;
    }

    public void setMyRating(String myRating) {
        this.myRating = myRating;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }


    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }


    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }


    public MovieModel(double voteAverage, String name, String image, String overview, String releaseDate, String tmdbId) {
        this.voteAverage = voteAverage;
        this.name = name;
        this.image = image;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.tmdbId = tmdbId;
    }

    public MovieModel(double voteAverage, String name, String image, String overview) {
        this.voteAverage = voteAverage;
        this.name = name;
        this.image = image;
        this.overview = overview;
    }

    public MovieModel() {
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeDouble(voteAverage);
        dest.writeString(name);
        dest.writeString(image);
        dest.writeString(overview);
        dest.writeString(releaseDate);
        dest.writeByte((byte) (favourite ? 1 : 0));
        dest.writeString(notes);
        dest.writeSerializable(genre);
        dest.writeString(myRating);
        dest.writeInt(ratingValue);
        dest.writeString(tmdbId);

    }

    private MovieModel(Parcel source) {
        id = source.readString();
        voteAverage = source.readDouble();
        name = source.readString();
        image = source.readString();
        overview = source.readString();
        releaseDate = source.readString();
        favourite = source.readByte() != 0;
        notes = source.readString();
        genre = (Genre) source.readSerializable();
        myRating = source.readString();
        ratingValue = source.readInt();
        tmdbId = source.readString();
    }

    public static Creator<MovieModel> CREATOR = new Creator<MovieModel>() {
        @Override
        public MovieModel createFromParcel(Parcel source) {
            return new MovieModel(source);
        }

        @Override
        public MovieModel[] newArray(int size) {
            return new MovieModel[size];
        }
    };

    public int getRatingValue() {
        return ratingValue;
    }

    public void setRatingValue(int ratingValue) {
        this.ratingValue = ratingValue;
    }

    public String getTmdbId() {
        return tmdbId;
    }

    public void setTmdbId(String tmdbId) {
        this.tmdbId = tmdbId;
    }

//    @Override
//    public boolean equals(Object obj) {
//        if (obj == this)
//            return true; // Same object in memory
//        if (!(obj instanceof MovieModel))
//            return false; // Not even the same class
//        final MovieModel other = (MovieModel) obj;
//        return name.equals(other.getName());
//    }
//
//    @Override
//    public int hashCode() {
//        return name.hashCode();
//    }


//    public String getGenre() {
//        return genre;
//    }
//
//    public void setGenre(String genre) {
//        this.genre = genre;
//    }
}
