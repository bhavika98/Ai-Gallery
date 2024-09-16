package com.app.incroyable.ai_gallery.model;

public class AlbumSetting {

    int imageType;
    String CoverImage, Path;
    int Column;
    String Order;
    boolean Hide;

    // Order :- Name Asc = 00, Name Desc = 01,
    //          Date Asc = 10, Date Desc = 11,
    //          Size Asc = 20, Size Desc = 21,
    //          Last Modified Asc = 30, Last Modified Desc = 31

    public AlbumSetting(int imageType, String coverImage, String path, int column, String order, boolean hide) {
        this.imageType = imageType;
        CoverImage = coverImage;
        Path = path;
        Column = column;
        Order = order;
        Hide = hide;
    }

    public int getImageType() {
        return imageType;
    }

    public void setImageType(int imageType) {
        this.imageType = imageType;
    }

    public String getCoverImage() {
        return CoverImage;
    }

    public void setCoverImage(String coverImage) {
        CoverImage = coverImage;
    }

    public String getPath() {
        return Path;
    }

    public void setPath(String path) {
        Path = path;
    }

    public int getColumn() {
        return Column;
    }

    public void setColumn(int column) {
        Column = column;
    }

    public String getOrder() {
        return Order;
    }

    public void setOrder(String order) {
        Order = order;
    }

    public boolean isHide() {
        return Hide;
    }

    public void setHide(boolean hide) {
        Hide = hide;
    }
}
