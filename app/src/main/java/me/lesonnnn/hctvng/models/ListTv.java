package me.lesonnnn.hctvng.models;

public class ListTv {
    private int mId;
    private String mTitle;

    public ListTv() {
    }

    public ListTv(String title) {
        mTitle = title;
    }

    public ListTv(int id, String title) {
        mId = id;
        mTitle = title;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }
}
