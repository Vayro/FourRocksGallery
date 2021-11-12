package com.vayrotech.fourrocksgallery.FolderFragmentStuff;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;

//images and folder path

public class Model_images implements Parcelable {
    String str_folder, str_folderPath, str_title;
    Date str_dateModified;
    ArrayList<String> al_imagepath;

    protected Model_images(Parcel in) {
        str_folder = in.readString();
        str_folderPath = in.readString();
        str_title = in.readString();
        al_imagepath = in.createStringArrayList();
    }

    public static final Creator<Model_images> CREATOR = new Creator<Model_images>() {
        @Override
        public Model_images createFromParcel(Parcel in) {
            return new Model_images(in);
        }

        @Override
        public Model_images[] newArray(int size) {
            return new Model_images[size];
        }
    };

    public Model_images() {

    }

    public String getStr_folder() {
        return str_folder;
    }

    public void setStr_folder(String str_folder) {
        this.str_folder = str_folder;
    }

    public ArrayList<String> getAl_imagepath() {
        return al_imagepath;
    }

    public void setAl_imagepath(ArrayList<String> al_imagepath) {
        this.al_imagepath = al_imagepath;
    }

    public String toString() {
        return str_folder;

    }

    public String getStr_folderPath() {
        return str_folderPath;
    }


    public void setStr_folderPath(String str_folderPath) {
        this.str_folderPath = str_folderPath;
    }


    public String getStr_title() {
        return str_title;
    }

    public void setStr_title(String str_title) {
        this.str_title = str_title;
    }

    public Date getStr_dateModified() {
        return str_dateModified;
    }

    public void setStr_dateModified(Date str_dateModified) {
        this.str_dateModified = str_dateModified;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(str_folder);
        parcel.writeString(str_folderPath);
        parcel.writeString(str_title);
        parcel.writeStringList(al_imagepath);
    }
}