package com.thefunteam.android.model.shared;

import android.graphics.Color;
import com.thefunteam.android.model.Cord;

public class MapHelper {
    public static Cord getLocation(City city) {
        switch (city) {
            case Atlanta:
                return new Cord(763, 395);
            case Boston:
                return new Cord(931, 105);
            case Calgary:
                return new Cord(193, 49);
            case Charleston:
                return new Cord(856, 403);
            case Chicago:
                return new Cord(663, 237);
            case Dallas:
                return new Cord(525, 490);
            case Denver:
                return new Cord(358, 331);
            case Duluth:
                return new Cord(540, 175);
            case ElPaso:
                return new Cord(345, 517);
            case Helena:
                return new Cord(301, 181);
            case Houston:
                return new Cord(570, 532);
            case KansasCity:
                return new Cord(529, 318);
            case LasVegas:
                return new Cord(174, 415);
            case LittleRock:
                return new Cord(598, 408);
            case LosAngeles:
                return new Cord(105, 472);
            case Miami:
                return new Cord(886, 558);
            case Montreal:
                return new Cord(858, 42);
            case Nashville:
                return new Cord(712, 357);
            case NewOrleans:
                return new Cord(664, 517);
            case NewYork:
                return new Cord(873, 174);
            case OklahomaCity:
                return new Cord(510, 408);
            case Omaha:
                return new Cord(510, 265);
            case Phoenix:
                return new Cord(229, 480);
            case Pittsburgh:
                return new Cord(787, 223);
            case Portland:
                return new Cord(43, 172);
            case Raleigh:
                return new Cord(826, 337);
            case SaintLouis:
                return new Cord(616, 319);
            case SaltLakeCity:
                return new Cord(231, 310);
            case SanFrancisco:
                return new Cord(30, 369);
            case SantaFe:
                return new Cord(351, 426);
            case SaultStMarie:
                return new Cord(666, 108);
            case Seattle:
                return new Cord(63, 120);
            case Toronto:
                return new Cord(776, 129);
            case Vancouver:
                return new Cord(70, 66);
            case Washington:
                return new Cord(885, 267);
            case Winnipeg:
                return new Cord(426, 57);
            default:
                return null;
        }
    }
    public static String getName(City city) {
        switch (city) {
            case Atlanta:
            case Boston:
            case Calgary:
            case Charleston:
            case Chicago:
            case Dallas:
            case Denver:
            case Duluth:
            case Helena:
            case Houston:
            case Miami:
            case Montreal:
            case Nashville:
            case Omaha:
            case Phoenix:
            case Pittsburgh:
            case Portland:
            case Raleigh:
            case Seattle:
            case Toronto:
            case Vancouver:
            case Washington:
            case Winnipeg:
                return city.name();
            case LasVegas:
                return "Las Vegas";
            case ElPaso:
                return "El Paso";
            case SaintLouis:
                return "Saint Louis";
            case SaltLakeCity:
                return "Salt Lake City";
            case SanFrancisco:
                return "San Fransisco";
            case SantaFe:
                return "Santa Fe";
            case NewYork:
                return "New York";
            case OklahomaCity:
                return "Oklahoma City";
            case NewOrleans:
                return "New Orleans";
            case KansasCity:
                return "Kansas City";
            case LittleRock:
                return "Little Rock";
            case LosAngeles:
                return "Los Angeles";
            case SaultStMarie:
                return "Sault St Marie";
            default:
                return "";
        }
    }

    public static int getColor(TrainType trainType) {
        switch (trainType) {
            case purple:
                return Color.rgb(234, 10, 290);
            case white:
                return Color.rgb(250, 250, 250);
            case blue:
                return Color.rgb(10, 20, 240);
            case yellow:
                return Color.rgb(160, 230, 30);
            case orange:
                return Color.rgb( 230, 240, 12);
            case black:
                return Color.rgb(20, 20, 20);
            case red:
                return Color.rgb(250, 10, 12);
            case green:
                return Color.rgb(14, 250, 5);
            case any:
                return Color.rgb(100,100,100);
            default:
                return 0;
        }
    }

    public static int getPlayerColor(int number) {
        switch (number) {
            case 1:
                return Color.BLUE; // blue
            case 2:
                return Color.GREEN;
            case 3:
                return Color.YELLOW;
            case 4:
                return Color.RED;
            case 5:
                return Color.BLACK;
            default:
                return 0;
        }
    }


    public static City[] allCities = {City.Atlanta,City.Boston,City.Calgary,City.Charleston,City.Chicago,City.Dallas,City.Denver,City.Duluth,City.ElPaso,City.Helena,City.Houston,City.KansasCity,City.LasVegas,City.LittleRock,City.LosAngeles,City.Miami,City.Montreal,City.Nashville,City.NewOrleans,City.NewYork,City.OklahomaCity,City.Omaha,City.Phoenix,City.Pittsburgh,City.Portland,City.Raleigh,City.SaintLouis,City.SaltLakeCity,City.SanFrancisco,City.SantaFe,City.SaultStMarie,City.Seattle,City.Toronto,City.Vancouver,City.Washington,City.Winnipeg};
}
