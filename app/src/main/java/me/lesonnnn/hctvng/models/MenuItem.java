package me.lesonnnn.hctvng.models;

import me.lesonnnn.hctvng.activities.MainActivity;

public class MenuItem {
    private MainActivity.MENU_ITEM id;
    private int resId;
    private String name;

    public MenuItem(MainActivity.MENU_ITEM itemId, int resId, String name) {
        this.id = itemId;
        this.resId = resId;
        this.name = name;
    }

    public MainActivity.MENU_ITEM getId() {
        return id;
    }

    public int getResId() {
        return resId;
    }

    public String getName() {
        return name;
    }
}
