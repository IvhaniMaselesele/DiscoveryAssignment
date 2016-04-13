package za.co.discovery.services;

import org.springframework.stereotype.Service;
import za.co.discovery.Models.*;

import java.util.LinkedList;

@Service
public class ShortestPathService {

    //private Graph graph;
    public ShortestPathService() {
    }


    public String convertPathToString(LinkedList<Vertex> path) {
        String stringPath = "";
        for (Vertex v : path) {
            stringPath += v.getName() + ",";
        }
        return stringPath;
    }

    public void addNode(Planet planet, Graph graph) {
        Vertex vertex = new Vertex(planet.getNode(), planet.getName());
        graph.addVertex(vertex);
    }

    public void addEdge(Route route, Graph graph) {

        Vertex originVertex = graph.getVertexById(route.getOrigin());
        Vertex destinationVertex = graph.getVertexById(route.getDestination());
        if (originVertex == null || destinationVertex == null) {
            System.out.println("Either your origin or destination does not exist");
        } else {
            Edge edge = new Edge("route_" + route.getId(), originVertex, destinationVertex, route.getDistance());
            graph.addEdge(edge);
        }
    }
    /*
        nodes = new ArrayList<Vertex>();
        edges = new ArrayList<Edge>();

        make vertices
        Vertex location = new Vertex("" + i, "" + i);
        nodes.add(location);

        Edge lane = new Edge(laneId, nodes.get(sourceLocNo), nodes.get(destLocNo), duration);
        edges.add(lane);


        Graph graph = new Graph(nodes, edges);
        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph);
        dijkstra.execute(nodes.get(0));
        LinkedList<Vertex> path = dijkstra.getPath(nodes.get(10));
        */


}
