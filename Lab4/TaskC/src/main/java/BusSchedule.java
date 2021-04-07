import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BusSchedule {
    private final List<CityStop> busStops;

    public BusSchedule() {
        busStops = new ArrayList<>();
    }

    public void addBusStop(String city) {
        busStops.add(new CityStop(city));
        System.out.println("Added bus stop: " + city);
    }

    public void deleteBusStop(String city) {
        CityStop cityStopToDelete = null;
        for (CityStop cityStop : busStops) {
            if (cityStop.getCity().equals(city)) {
                cityStopToDelete = cityStop;
                break;
            }
        }
        if (cityStopToDelete == null) {
            return;
        } else {
            Set<CityStop> stopsConnectedToDeleted = new HashSet<>(
                    cityStopToDelete.getNeighbors().keySet());
            for (CityStop cityStop : stopsConnectedToDeleted) {
                cityStopToDelete.removeConnection(cityStop);
            }
            busStops.remove(cityStopToDelete);
        }
        System.out.println("Deleted bus stop: " + city);
    }

    public void changeTripPrice(String firstCity, String secondCity, int price) {
        CityStop firstCityStopToChange = getCityStopByName(firstCity);
        CityStop secondCityStopToChange = getCityStopByName(secondCity);
        if (firstCityStopToChange != null && secondCity != null
                && firstCityStopToChange.isConnected(secondCityStopToChange)) {
            firstCityStopToChange.connect(secondCityStopToChange, price);
            System.out.println("Changed price for trip from " + firstCity + " to " + secondCity
                    + " for " + price);
        }
    }

    public void addTrip(String firstCity, String secondCity, int price) {
        CityStop firstCityStopToChange = getCityStopByName(firstCity);
        CityStop secondCityStopToChange = getCityStopByName(secondCity);
        if (firstCityStopToChange != null && secondCity != null) {
            firstCityStopToChange.connect(secondCityStopToChange, price);
            System.out.println("Added trip from " + firstCity + " to " + secondCity
                    + " with price " + price);
        }
    }

    public void deleteTrip(String firstCity, String secondCity) {
        CityStop firstCityStopToChange = getCityStopByName(firstCity);
        CityStop secondCityStopToChange = getCityStopByName(secondCity);
        if (firstCityStopToChange != null && secondCity != null
                && firstCityStopToChange.isConnected(secondCityStopToChange)) {
            firstCityStopToChange.removeConnection(secondCityStopToChange);
            System.out.println("Deleted trip from " + firstCity + " to " + secondCity);
        }
    }

    public Integer getTripPrice(String firstCity, String secondCity) {
        CityStop firstCityStopToChange = getCityStopByName(firstCity);
        CityStop secondCityStopToChange = getCityStopByName(secondCity);
        if (firstCityStopToChange == null || secondCityStopToChange == null) {
            throw new IllegalArgumentException("No such bus stop");
        }

        return firstCityStopToChange.getPrice(secondCityStopToChange);
    }

    private CityStop getCityStopByName(String cityName) {
        CityStop cityStop = new CityStop(cityName);
        if (busStops.contains(cityStop)) {
            return busStops.get(busStops.indexOf(cityStop));
        }
        return null;
    }
}
