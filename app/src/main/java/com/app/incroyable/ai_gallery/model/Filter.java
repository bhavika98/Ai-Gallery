package com.app.incroyable.ai_gallery.model;

public class Filter {

    int thumb;
    String title;

    public Filter(int thumb, String title) {
        this.thumb = thumb;
        this.title = title;
    }

    public int getThumb() {
        return thumb;
    }

    public void setThumb(int thumb) {
        this.thumb = thumb;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
