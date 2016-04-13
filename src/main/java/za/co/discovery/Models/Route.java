package za.co.discovery.Models;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@javax.persistence.Entity
@Table(name = "route")
public class Route {
    @Id
    @Column(nullable = false)
    int id;

    @Column
    private String origin;

    @Column
    private String destination;

    @Column
    private double distance;
    @Column
    private double traffic;

    protected Route() {

    }

    public Route(int id, String origin, String destination, double distance) {
        this.id = id;
        this.origin = origin;
        this.destination = destination;
        this.distance = distance;
    }

    @Override
    public String toString() {
        return Integer.toString(id) + "\t" + origin + "\t" + destination + "\t" + distance;
    }

    public double getDistance() {
        return distance;
    }

    public int getId() {
        return id;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public void setTraffic(double traffic) {
        this.traffic = traffic;
    }

    public double getTraffic() {
        return traffic;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }
}
