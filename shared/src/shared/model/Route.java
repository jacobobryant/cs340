package shared.model;

import java.io.Serializable;
import java.util.*;

public class Route implements Serializable {
    public static final List<Route> ROUTES;
    public static final Map<Integer, Integer> POINTS;
    static {
        List<Route> routes = new ArrayList<>();
        routes.add(new Route(City.Atlanta, City.Raleigh, TrainType.any, 2));
        routes.add(new Route(City.Atlanta, City.Raleigh, TrainType.any, 2, true));
        routes.add(new Route(City.Atlanta, City.Charleston, TrainType.any, 2));
        routes.add(new Route(City.Atlanta, City.Miami, TrainType.blue, 5));
        routes.add(new Route(City.Atlanta, City.NewOrleans, TrainType.yellow, 4));
        routes.add(new Route(City.Atlanta, City.NewOrleans, TrainType.orange, 4, true));
        routes.add(new Route(City.Atlanta, City.Nashville, TrainType.any, 1));
        routes.add(new Route(City.Boston, City.Montreal, TrainType.any, 2));
        routes.add(new Route(City.Boston, City.Montreal, TrainType.any, 2, true));
        routes.add(new Route(City.Boston, City.NewYork, TrainType.yellow, 2));
        routes.add(new Route(City.Boston, City.NewYork, TrainType.red, 2, true));
        routes.add(new Route(City.Calgary, City.Vancouver, TrainType.any, 3));
        routes.add(new Route(City.Calgary, City.Winnipeg, TrainType.white, 6));
        routes.add(new Route(City.Calgary, City.Helena, TrainType.any, 4));
        routes.add(new Route(City.Calgary, City.Seattle, TrainType.any, 4));
        routes.add(new Route(City.Charleston, City.Raleigh, TrainType.any, 2));
        routes.add(new Route(City.Charleston, City.Atlanta, TrainType.any, 2));
        routes.add(new Route(City.Charleston, City.Miami, TrainType.purple, 4));
        routes.add(new Route(City.Chicago, City.Pittsburgh, TrainType.orange, 3));
        routes.add(new Route(City.Chicago, City.Pittsburgh, TrainType.black, 3, true));
        routes.add(new Route(City.Chicago, City.Toronto, TrainType.white, 4));
        routes.add(new Route(City.Chicago, City.Duluth, TrainType.red, 3));
        routes.add(new Route(City.Chicago, City.Omaha, TrainType.blue, 4));
        routes.add(new Route(City.Chicago, City.SaintLouis, TrainType.green, 2));
        routes.add(new Route(City.Chicago, City.SaintLouis, TrainType.white, 2, true));
        routes.add(new Route(City.Dallas, City.LittleRock, TrainType.any, 2));
        routes.add(new Route(City.Dallas, City.OklahomaCity, TrainType.any, 2));
        routes.add(new Route(City.Dallas, City.OklahomaCity, TrainType.any, 2, true));
        routes.add(new Route(City.Dallas, City.ElPaso, TrainType.red, 4));
        routes.add(new Route(City.Dallas, City.Houston, TrainType.any, 1));
        routes.add(new Route(City.Dallas, City.Houston, TrainType.any, 1, true));
        routes.add(new Route(City.Denver, City.KansasCity, TrainType.black, 4));
        routes.add(new Route(City.Denver, City.KansasCity, TrainType.orange, 4, true));
        routes.add(new Route(City.Denver, City.Omaha, TrainType.purple, 4));
        routes.add(new Route(City.Denver, City.Helena, TrainType.green, 4));
        routes.add(new Route(City.Denver, City.SaltLakeCity, TrainType.red, 3));
        routes.add(new Route(City.Denver, City.SaltLakeCity, TrainType.yellow, 3, true));
        routes.add(new Route(City.Denver, City.Phoenix, TrainType.white, 5));
        routes.add(new Route(City.Denver, City.SantaFe, TrainType.any, 2));
        routes.add(new Route(City.Denver, City.OklahomaCity, TrainType.red, 4));
        routes.add(new Route(City.Duluth, City.Omaha, TrainType.any, 2));
        routes.add(new Route(City.Duluth, City.Omaha, TrainType.any, 2, true));
        routes.add(new Route(City.Duluth, City.Chicago, TrainType.red, 3));
        routes.add(new Route(City.Duluth, City.Toronto, TrainType.purple, 6));
        routes.add(new Route(City.Duluth, City.SaultStMarie, TrainType.any, 3));
        routes.add(new Route(City.Duluth, City.Winnipeg, TrainType.black, 4));
        routes.add(new Route(City.Duluth, City.Helena, TrainType.orange, 6));
        routes.add(new Route(City.ElPaso, City.Houston, TrainType.green, 6));
        routes.add(new Route(City.ElPaso, City.OklahomaCity, TrainType.yellow, 5));
        routes.add(new Route(City.ElPaso, City.SantaFe, TrainType.any, 2));
        routes.add(new Route(City.ElPaso, City.Phoenix, TrainType.any, 3));
        routes.add(new Route(City.ElPaso, City.LosAngeles, TrainType.black, 6));
        routes.add(new Route(City.Helena, City.Winnipeg, TrainType.blue, 4));
        routes.add(new Route(City.Helena, City.Omaha, TrainType.red, 5));
        routes.add(new Route(City.Helena, City.SaltLakeCity, TrainType.purple, 3));
        routes.add(new Route(City.Helena, City.Seattle, TrainType.yellow, 6));
        routes.add(new Route(City.Houston, City.NewOrleans, TrainType.any, 2));
        routes.add(new Route(City.KansasCity, City.SaintLouis, TrainType.blue, 2));
        routes.add(new Route(City.KansasCity, City.SaintLouis, TrainType.purple, 2, true));
        routes.add(new Route(City.KansasCity, City.Omaha, TrainType.any, 1));
        routes.add(new Route(City.KansasCity, City.Omaha, TrainType.any, 1, true));
        routes.add(new Route(City.KansasCity, City.OklahomaCity, TrainType.any, 2));
        routes.add(new Route(City.KansasCity, City.OklahomaCity, TrainType.any, 2, true));
        routes.add(new Route(City.LasVegas, City.SaltLakeCity, TrainType.orange, 3));
        routes.add(new Route(City.LasVegas, City.LosAngeles, TrainType.any, 2));
        routes.add(new Route(City.LittleRock, City.Nashville, TrainType.white, 3));
        routes.add(new Route(City.LittleRock, City.SaintLouis, TrainType.any, 2));
        routes.add(new Route(City.LittleRock, City.OklahomaCity, TrainType.any, 2));
        routes.add(new Route(City.LittleRock, City.NewOrleans, TrainType.green, 3));
        routes.add(new Route(City.LosAngeles, City.SanFrancisco, TrainType.purple, 3));
        routes.add(new Route(City.LosAngeles, City.SanFrancisco, TrainType.yellow, 3, true));
        routes.add(new Route(City.LosAngeles, City.Phoenix, TrainType.any, 3));
        routes.add(new Route(City.Miami, City.NewOrleans, TrainType.red, 6));
        routes.add(new Route(City.Montreal, City.NewYork, TrainType.blue, 3));
        routes.add(new Route(City.Montreal, City.Toronto, TrainType.any, 3));
        routes.add(new Route(City.Montreal, City.SaultStMarie, TrainType.black, 5));
        routes.add(new Route(City.Nashville, City.Raleigh, TrainType.black, 3));
        routes.add(new Route(City.Nashville, City.Pittsburgh, TrainType.yellow, 4));
        routes.add(new Route(City.Nashville, City.SaintLouis, TrainType.any, 2));
        routes.add(new Route(City.NewYork, City.Washington, TrainType.black, 2));
        routes.add(new Route(City.NewYork, City.Washington, TrainType.orange, 2, true));
        routes.add(new Route(City.NewYork, City.Pittsburgh, TrainType.white, 2));
        routes.add(new Route(City.NewYork, City.Pittsburgh, TrainType.green, 2, true));
        routes.add(new Route(City.OklahomaCity, City.SantaFe, TrainType.blue, 3));
        routes.add(new Route(City.Phoenix, City.SantaFe, TrainType.any, 3));
        routes.add(new Route(City.Pittsburgh, City.Washington, TrainType.any, 2));
        routes.add(new Route(City.Pittsburgh, City.Raleigh, TrainType.any, 2));
        routes.add(new Route(City.Pittsburgh, City.SaintLouis, TrainType.green, 5));
        routes.add(new Route(City.Pittsburgh, City.Toronto, TrainType.any, 2));
        routes.add(new Route(City.Portland, City.Seattle, TrainType.any, 1));
        routes.add(new Route(City.Portland, City.Seattle, TrainType.any, 1, true));
        routes.add(new Route(City.Portland, City.SaltLakeCity, TrainType.blue, 6));
        routes.add(new Route(City.Portland, City.SanFrancisco, TrainType.green, 5));
        routes.add(new Route(City.Portland, City.SanFrancisco, TrainType.purple, 5, true));
        routes.add(new Route(City.Raleigh, City.Washington, TrainType.any, 2));
        routes.add(new Route(City.Raleigh, City.Washington, TrainType.any, 2, true));
        routes.add(new Route(City.SaltLakeCity, City.SanFrancisco, TrainType.orange, 5));
        routes.add(new Route(City.SaltLakeCity, City.SanFrancisco, TrainType.white, 5, true));
        routes.add(new Route(City.SaultStMarie, City.Winnipeg, TrainType.any, 6));
        routes.add(new Route(City.SaultStMarie, City.Toronto, TrainType.any, 2));
        routes.add(new Route(City.Seattle, City.Vancouver, TrainType.any, 1));
        routes.add(new Route(City.Seattle, City.Vancouver, TrainType.any, 1, true));
        ROUTES = Collections.unmodifiableList(routes);

        POINTS = new HashMap<>();
        POINTS.put(1, 1);
        POINTS.put(2, 1);
        POINTS.put(3, 4);
        POINTS.put(4, 7);
        POINTS.put(5, 10);
        POINTS.put(6, 15);
    }
    public final City city1;
    public final City city2;
    public final TrainType type;
    public final int length;
    public final boolean second;

    public Route(City city1, City city2, TrainType type, int length, boolean second) {
        this.city1 = city1;
        this.city2 = city2;
        this.type = type;
        this.length = length;
        this.second = second;
    }

    public Route(City city1, City city2, TrainType type, int length) {
        this(city1, city2, type, length, false);
    }

    public boolean hasDouble() {
        return ROUTES.contains(fooDouble());
    }

    // if it's called getDouble, GSON tries to serialize it as a field which
    // causes an infinite recursion.
    public Route fooDouble() {
        return new Route(city1, city2, type, length, !second);
    }

    public int points() {
        return POINTS.get(length);
    }

    public boolean match(City a, City b) {
        return ((city1 == a && city2 == b) || (city1 == b && city2 == a));
    }

    public boolean match(City a) {
        return city1 == a || city2 == a;
    }

    public City other(City a) {
        return ((a == city1) ? city2 : city1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Route route = (Route) o;

        if (length != route.length) return false;
        if (second != route.second) return false;
        if (city1 != route.city1) return false;
        if (city2 != route.city2) return false;
        return type == route.type;
    }

    @Override
    public int hashCode() {
        int result = city1.hashCode();
        result = 31 * result + city2.hashCode();
        result = 31 * result + type.hashCode();
        result = 31 * result + length;
        result = 31 * result + (second ? 1 : 0);
        return result;
    }
}
