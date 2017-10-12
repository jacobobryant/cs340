package com.thefunteam.android.model.InGameModel;

import java.util.List;

public class Route {
    //determine this route is claimed or not
    private boolean claimed;
    //the master of the route
    private String master;
    //private cities that is connected by the route
    private List<City> connectedCity;
}
