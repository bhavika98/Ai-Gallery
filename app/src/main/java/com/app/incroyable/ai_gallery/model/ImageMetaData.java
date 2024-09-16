package com.app.incroyable.ai_gallery.model;

public class ImageMetaData {

    int Id;
    String Name, Desc;

    public ImageMetaData(int id, String name, String desc) {
        Id = id;
        Name = name;
        Desc = desc;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDesc() {
        return Desc;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }
}
