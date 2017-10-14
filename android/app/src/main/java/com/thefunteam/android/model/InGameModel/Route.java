package com.thefunteam.android.model.InGameModel;

import java.util.List;

public class Route {
    //the master of the route
    private String master;
    //private cities that is connected by the route
    private City city1;
    private City city2;

    public Route(String master, City city1, City city2) {
        this.master = master;
        this.city1 = city1;
        this.city2 = city2;
    }

    public String getMaster() {
        return master;
    }

    public City getCity1() {
        return city1;
    }

    public City getCity2() {
        return city2;
    }

}
