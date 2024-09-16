package com.app.incroyable.ai_gallery.model;

public class Crop {

    int image;
    String text;
    int left, right;

    public Crop(int image, String text, int left, int right) {
        this.image = image;
        this.text = text;
        this.left = left;
        this.right = right;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getRight() {
        return right;
    }

    public void setRight(int right) {
        this.right = right;
    }
}
