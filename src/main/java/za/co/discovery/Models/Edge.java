package za.co.discovery.Models;

public class Edge {
    public String edgeId;
    public Vertex source;
    public Vertex destination;
    public double weight;

    public double traffic;

    public String destinationId;

    public Edge(String edgeId, Vertex source, Vertex destination, double weight) {
        this.edgeId = edgeId;
        this.source = source;
        this.destination = destination;
        this.weight = weight;
    }


    public Edge() {

    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(String destinationId) {
        this.destinationId = destinationId;
    }

    public String getEdgeId() {
        return edgeId;
    }

    public void setEdgeId(String edgeId) {
        this.edgeId = edgeId;
    }

    public void setDestination(Vertex destination) {
        this.destination = destination;
    }

    public void setSource(Vertex source) {
        this.source = source;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public Vertex getDestination() {
        return destination;
    }


    public Vertex getSource() {
        return source;
    }

    public double getWeight() {
        return weight;
    }

    public String sourceId;

    public double getTraffic() {
        return traffic;
    }

    public void setTraffic(double traffic) {
        this.traffic = traffic;
    }

    @Override
    public String toString() {
        return source + " hj " + destination;
    }


}