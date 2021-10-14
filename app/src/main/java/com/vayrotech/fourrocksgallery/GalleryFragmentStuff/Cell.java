package com.vayrotech.fourrocksgallery.GalleryFragmentStuff;

public class Cell implements Comparable< Cell >{
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

//start programming sorting shit
    public int compareTo(Cell c){
        return this.getDate().compareTo(c.getDate());

    }


}
