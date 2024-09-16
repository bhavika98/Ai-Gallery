package com.app.incroyable.ai_gallery.model;

import java.io.Serializable;

public class ThumbImage implements Serializable {

    String Loc, Path, Date, Name, Duration;
    long Size;
    int Type, Position, Id;

    // Type :- Photo = 0 & Video = 1 & GIF = 2

    public ThumbImage(String loc, String path, String date, String name, String duration, long size, int type, int position, int id) {
        Loc = loc;
        Path = path;
        Date = date;
        Name = name;
        Duration = duration;
        Size = size;
        Type = type;
        Position = position;
        Id = id;
    }

    public String getLoc() {
        return Loc;
    }

    public void setLoc(String loc) {
        Loc = loc;
    }

    public String getPath() {
        return Path;
    }

    public void setPath(String path) {
        Path = path;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDuration() {
        return Duration;
    }

    public void setDuration(String duration) {
        Duration = duration;
    }

    public long getSize() {
        return Size;
    }

    public void setSize(long size) {
        Size = size;
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    public int getPosition() {
        return Position;
    }

    public void setPosition(int position) {
        Position = position;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }
}
