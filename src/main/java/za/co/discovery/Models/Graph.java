package za.co.discovery.Models;

import java.util.ArrayList;
import java.util.List;

public class Graph {
    private final List<Vertex> vertexes;
    private final List<Edge> edges;

    public Graph(List<Vertex> vertexes, List<Edge> edges) {
        this.vertexes = vertexes;
        this.edges = edges;
    }

    public Graph() {
        this.vertexes = new ArrayList<Vertex>();
        this.edges = new ArrayList<Edge>();
    }


    public List<Vertex> getVertexes() {
        return vertexes;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public void addEdge(Edge e) {
        edges.add(e);
    }

    public void addVertex(Vertex e) {
        vertexes.add(e);
    }

    public Vertex getVertexById(String node) {
        for (Vertex vertex : vertexes) {
            if (node.equals(vertex.getId())) {
                return vertex;
            }
        }
        return null;
    }


}
