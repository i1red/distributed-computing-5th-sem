import java.util.*;

public class CityStop {
    private String city;
    private Map<CityStop, Integer> neighbors;

    public CityStop(String city) {
        this.city = city;
        this.neighbors = new HashMap<>();
    }

    public void connect(CityStop node, int price) {
        if (this == node) {
            throw new IllegalArgumentException("Can't ride in within one city");
        }
        this.neighbors.put(node, price);
        node.neighbors.put(this, price);
    }

    public void removeConnection(CityStop node) {
        neighbors.remove(node);
        node.neighbors.remove(this);
    }

    public boolean isConnected(CityStop node) {
        return neighbors.containsKey(node);
    }

    public Map<CityStop, Integer> getNeighbors() {
        return neighbors;
    }

    public String getCity() {
        return city;
    }

    public Integer getPrice(CityStop other) {
        List<CityStop> path = this.getPath(other);
        if (path == null) {
            return null;
        }

        Integer price = 0;
        CityStop currentCity = this;
        for (CityStop cityStop: path) {
            price += currentCity.getNeighbors().get(cityStop);
            currentCity = cityStop;
        }

        return price;
    }

    public List<CityStop> getPath(CityStop other) {
        if (this.equals(other)) {
            return new LinkedList<>();
        }

        Map<CityStop, CityStop> visited = new HashMap<>(){{put(CityStop.this, null);}};
        Queue<CityStop> cityStopQueue = new LinkedList<>(Collections.singletonList(this));

        while (!cityStopQueue.isEmpty()) {
            CityStop currentStop = cityStopQueue.poll();
            if (currentStop.equals(other)) {
                break;
            }

            for (CityStop cityStop: currentStop.getNeighbors().keySet()) {
                if (!visited.containsKey(cityStop)) {
                    visited.put(cityStop, currentStop);
                    cityStopQueue.add(cityStop);
                }
            }
        }

        if (!visited.containsKey(other)) {
            return null;
        }

        LinkedList<CityStop> path = new LinkedList<>();
        CityStop cityStop = other;
        while (!cityStop.equals(this)) {
            path.addFirst(cityStop);
            cityStop = visited.get(cityStop);
        }
        return path;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null || this.getClass() != other.getClass()) {
            return false;
        }

        return this.city.equals(((CityStop) other).city);
    }
}
