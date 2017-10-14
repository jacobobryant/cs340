package com.thefunteam.android.model.InGameModel;

public class DestCard {
    //type represents type of the card.
    private String type;

    //DestCard has holds two city that makes each others are destination
    private City city1;
    private City city2;

    public DestCard(String type, City city1, City city2) {
        this.type = type;
        this.city1 = city1;
        this.city2 = city2;
    }

    public String getType() {
        return type;
    }

    public City getCity1() {
        return city1;
    }

    public City getCity2() {
        return city2;
    }

}
