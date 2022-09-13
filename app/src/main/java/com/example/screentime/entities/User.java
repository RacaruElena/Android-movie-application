package com.example.screentime.entities;

import java.util.ArrayList;
import java.util.List;

public class User {
    public String id;
    public String fullName;
    public String email;
    public String phoneNumber;
    public String password;
    public boolean isPublicList;
    private List<MovieModel> movies;

    public String getId() {
        return id;
    }

    public boolean isPublicList() {
        return isPublicList;
    }

    public void setPublicList(boolean publicList) {
        isPublicList = publicList;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    public void setEmail(String email) {
        this.email = email;
    }


    public User() {
    }


    public User(String id, String fullName) {
        this.id = id;
        this.fullName = fullName;
    }

    public User(String fullName, String email, String password, String phoneNumber) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        //movies = new ArrayList<>();
    }

    public List<MovieModel> getMovies() {
        return movies;
    }

    public void setMovies(List<MovieModel> movies) {
        this.movies = movies;
    }
}
