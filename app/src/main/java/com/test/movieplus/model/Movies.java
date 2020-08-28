package com.test.movieplus.model;

public class Movies {

    private int id;
    private String title;
    private String year;
    private String photo_url;
    private boolean isChecked;


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
