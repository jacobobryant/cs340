package com.thefunteam.android;

import com.thefunteam.android.model.Game;

/**
 * Created by msi on 2017-10-01.
 */

public class ListItem {
    private Game description;

    public ListItem(Game description) {
        this.description = description;
    }

    public Game getDescription() {
        return description;
    }

    public void setDescription(Game description) {
        this.description = description;
    }
}
