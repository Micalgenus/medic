package com.example.han.medic;

/**
 * Created by 송재형 on 2016-12-02.
 */

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