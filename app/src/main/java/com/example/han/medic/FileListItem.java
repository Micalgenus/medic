package com.example.han.medic;

public class FileListItem {
    private String id;
    private String date;

    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public FileListItem(String id, String date) {
        this.id = id;
        this.date = date;
    }
}