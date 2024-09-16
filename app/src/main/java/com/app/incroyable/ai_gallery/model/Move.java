package com.app.incroyable.ai_gallery.model;

public class Move {

    String Path, Name;
    int Image, Storage;

    public Move(String path, String name, int image, int storage) {
        Path = path;
        Name = name;
        Image = image;
        Storage = storage;
    }

    public String getPath() {
        return Path;
    }

    public void setPath(String path) {
        Path = path;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getImage() {
        return Image;
    }

    public void setImage(int image) {
        Image = image;
    }

    public int getStorage() {
        return Storage;
    }

    public void setStorage(int storage) {
        Storage = storage;
    }
}
