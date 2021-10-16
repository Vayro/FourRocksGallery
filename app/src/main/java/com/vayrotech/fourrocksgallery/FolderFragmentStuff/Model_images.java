package com.vayrotech.fourrocksgallery.FolderFragmentStuff;

import java.util.ArrayList;

//images and folder path

public class Model_images {
    String str_folder, str_folderPath;
    ArrayList<String> al_imagepath;

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


}