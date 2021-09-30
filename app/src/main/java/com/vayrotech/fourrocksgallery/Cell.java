package com.vayrotech.fourrocksgallery;

public class Cell {
    private String title, path, date;

    public String getTitle() {

        return title;
    }

    public void setTitle(String title) {

        this.title = title;
    }

    public String getPath() {

        return path;
    }

    public void setPath(String path) {

        this.path = path;
    }

    public String getDate() {

        return date;
    }

    public void setDate(String date) {

        this.date = date;
    }

    public String toString(){
    return title + " | " + date + " | " + path;

}


}
