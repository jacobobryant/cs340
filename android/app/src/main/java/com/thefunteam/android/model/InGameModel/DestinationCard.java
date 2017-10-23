package com.thefunteam.android.model.InGameModel;

public class DestinationCard {
    //type represents type of the card.
    private int points;

    //DestinationCard has holds two city that makes each others are destination
    private City city1;
    private City city2;

    public DestinationCard(int points, City city1, City city2) {
        this.points = points;
        this.city1 = city1;
        this.city2 = city2;
    }

    public int getPoints() {
        return points;
    }

    public City getCity1() {
        return city1;
    }

    public City getCity2() {
        return city2;
    }
}
