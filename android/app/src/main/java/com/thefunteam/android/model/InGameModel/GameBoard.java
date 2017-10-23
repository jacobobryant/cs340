package com.thefunteam.android.model.InGameModel;

import java.util.List;

public class GameBoard {
    //the place that actually game goes on

    //list of cities
    private List<City> cities;
    //list of routes
    private List<Route> routes;


    public GameBoard(List<City> cities, List<Route> routes) {
        this.cities = cities;
        this.routes = routes;
    }

    public List<City> getCities() {
        return cities;
    }

    public List<Route> getRoutes() {
        return routes;
    }

}
