package com.test.movieplus.model;

import java.io.Serializable;

public class Movies implements Serializable {

    private int id;
    private String title;
    private String year;
    private String overview;
    private String photo_url;
    private String backdrop_url;
    private boolean isChecked;

    public Movies(int id, String title, String year, String overview, String photo_url, String backdrop_url, boolean isChecked) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.overview = overview;
        this.photo_url = photo_url;
        this.backdrop_url = backdrop_url;
        this.isChecked = isChecked;
    }

    public Movies(int id, String title, String year, String overview, String photo_url, String backdrop_url) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.overview = overview;
        this.photo_url = photo_url;
        this.backdrop_url = backdrop_url;
    }



    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getBackdrop_url() {
        return backdrop_url;
    }

    public void setBackdrop_url(String backdrop_url) {
        this.backdrop_url = backdrop_url;
    }

    public Movies(int id, String title, String year, String photo_url, boolean isChecked) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.photo_url = photo_url;
        this.isChecked = isChecked;
    }

    public Movies() {
    }

    public Movies(int id, String title, String year, String photo_url) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.photo_url = photo_url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getPhoto_url() {
        return photo_url;
    }

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
