package com.app.incroyable.ai_gallery.model;

public class Folder {

    String Name, Date, Path, FrontImage;
    int Storage, Total_Photo, Total_Video, Position;
    boolean Hide;
    int FrontThumbType;

    // Storage :- Internal = 0, External = 1

    public Folder(String name, String date, String path, String frontImage, int storage, int total_Photo, int total_Video, int position, boolean hide, int frontThumbType) {
        Name = name;
        Date = date;
        Path = path;
        FrontImage = frontImage;
        Storage = storage;
        Total_Photo = total_Photo;
        Total_Video = total_Video;
        Position = position;
        Hide = hide;
        FrontThumbType = frontThumbType;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getPath() {
        return Path;
    }

    public void setPath(String path) {
        Path = path;
    }

    public String getFrontImage() {
        return FrontImage;
    }

    public void setFrontImage(String frontImage) {
        FrontImage = frontImage;
    }

    public int getStorage() {
        return Storage;
    }

    public void setStorage(int storage) {
        Storage = storage;
    }

    public int getTotal_Photo() {
        return Total_Photo;
    }

    public void setTotal_Photo(int total_Photo) {
        Total_Photo = total_Photo;
    }

    public int getTotal_Video() {
        return Total_Video;
    }

    public void setTotal_Video(int total_Video) {
        Total_Video = total_Video;
    }

    public int getPosition() {
        return Position;
    }

    public void setPosition(int position) {
        Position = position;
    }

    public boolean isHide() {
        return Hide;
    }

    public void setHide(boolean hide) {
        Hide = hide;
    }

    public int getFrontThumbType() {
        return FrontThumbType;
    }

    public void setFrontThumbType(int frontThumbType) {
        FrontThumbType = frontThumbType;
    }
}
