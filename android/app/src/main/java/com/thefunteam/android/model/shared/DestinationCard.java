package com.thefunteam.android.model.shared;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DestinationCard {
    public static final List<DestinationCard> DECK;
    static {
        List<DestinationCard> ddeck = new ArrayList<>();
        ddeck.add(new DestinationCard(City.Denver, City.ElPaso, 4));
        ddeck.add(new DestinationCard(City.KansasCity, City.Houston, 5));
        ddeck.add(new DestinationCard(City.NewYork, City.Atlanta, 6));
        ddeck.add(new DestinationCard(City.Chicago, City.NewOrleans, 7));
        ddeck.add(new DestinationCard(City.Calgary, City.SaltLakeCity, 7));
        ddeck.add(new DestinationCard(City.Helena, City.LosAngeles, 8));
        ddeck.add(new DestinationCard(City.Duluth, City.Houston, 8));
        ddeck.add(new DestinationCard(City.SaultStMarie, City.Nashville, 8));
        ddeck.add(new DestinationCard(City.Montreal, City.Atlanta, 9));
        ddeck.add(new DestinationCard(City.SaultStMarie, City.OklahomaCity, 9));
        ddeck.add(new DestinationCard(City.Seattle, City.LosAngeles, 9));
        ddeck.add(new DestinationCard(City.Chicago, City.SantaFe, 9));
        ddeck.add(new DestinationCard(City.Duluth, City.ElPaso, 10));
        ddeck.add(new DestinationCard(City.Toronto, City.Miami, 10));
        ddeck.add(new DestinationCard(City.Portland, City.Phoenix, 11));
        ddeck.add(new DestinationCard(City.Dallas, City.NewYork, 11));
        ddeck.add(new DestinationCard(City.Denver, City.Pittsburgh, 11));
        ddeck.add(new DestinationCard(City.Winnipeg, City.LittleRock, 11));
        ddeck.add(new DestinationCard(City.Winnipeg, City.Houston, 12));
        ddeck.add(new DestinationCard(City.Boston, City.Miami, 12));
        ddeck.add(new DestinationCard(City.Vancouver, City.SantaFe, 13));
        ddeck.add(new DestinationCard(City.Calgary, City.Phoenix, 13));
        ddeck.add(new DestinationCard(City.Montreal, City.NewOrleans, 13));
        ddeck.add(new DestinationCard(City.LosAngeles, City.Chicago, 16));
        ddeck.add(new DestinationCard(City.SanFrancisco, City.Atlanta, 17));
        ddeck.add(new DestinationCard(City.Portland, City.Nashville, 17));
        ddeck.add(new DestinationCard(City.Vancouver, City.Montreal, 20));
        ddeck.add(new DestinationCard(City.LosAngeles, City.Miami, 20));
        ddeck.add(new DestinationCard(City.LosAngeles, City.NewYork, 21));
        ddeck.add(new DestinationCard(City.Seattle, City.NewYork, 22));
        DECK = Collections.unmodifiableList(ddeck);
    }
    public final City city1;
    public final City city2;
    public final int points;

    public DestinationCard(City city1, City city2, int points) {
        this.city1 = city1;
        this.city2 = city2;
        this.points = points;
    }

    public static List<DestinationCard> getDECK() {
        return DECK;
    }

    public City getCity1() {
        return city1;
    }

    public City getCity2() {
        return city2;
    }

    public int getPoints() {
        return points;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DestinationCard that = (DestinationCard) o;

        if (points != that.points) return false;
        if (city1 != that.city1) return false;
        return city2 == that.city2;
    }

    @Override
    public int hashCode() {
        int result = city1 != null ? city1.hashCode() : 0;
        result = 31 * result + (city2 != null ? city2.hashCode() : 0);
        result = 31 * result + points;
        return result;
    }

    @Override
    public String toString() {
        return "DestinationCard{" +
                "city1=" + city1 +
                ", city2=" + city2 +
                ", points=" + points +
                '}';
    }

    public String description() {
        return MapHelper.getName(city1) + " to " + MapHelper.getName(city2) + "(" + Integer.toString(getPoints()) + ")";
    }
}
