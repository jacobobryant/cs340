package com.thefunteam.android.model.InGameModel;

import java.util.List;

public class Route {

    private City city1;
    private City city2;
    private TrainType type;
    private int length;

    public Route(City city1, City city2, TrainType type, int length) {
        this.city1 = city1;
        this.city2 = city2;
        this.type = type;
        this.length = length;
    }

    public City getCity1() {
        return city1;
    }

    public City getCity2() {
        return city2;
    }

    public TrainType getType() {
        return type;
    }

    public int getLength() {
        return length;
    }
}
