package za.co.discovery.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.co.discovery.Models.Edge;
import za.co.discovery.Models.Graph;
import za.co.discovery.Models.Planet;
import za.co.discovery.Models.Vertex;
import za.co.discovery.dataAccess.PlanetDAO;

import java.util.List;

@Service
public class PlanetService {

    PlanetDAO planetDAO;

    @Autowired
    public PlanetService(PlanetDAO planetDAO) {
        this.planetDAO = planetDAO;
    }


    public Planet createNewPlanet(String node, String name) {
        return new Planet(node, name);
    }

    public Planet persistPlanet(Planet planet) {
        return planetDAO.save(planet);
    }

    public Vertex addPlanetToGraphAsVertex(Graph graph, Planet planet) {
        Vertex vertex = new Vertex(planet.getNode(), planet.getName());
        graph.addVertex(vertex);
        return graph.getVertexById(vertex.getId());
    }

    public Vertex updateVertex(Graph graph, Planet planet) {
        Vertex vertex = graph.getVertexById(planet.getNode());
        vertex.setName(planet.getName());
        return vertex;
    }

    public void deleteVertex(Graph graph, Vertex vertexById) {
        DijkstraAlgorithm dijkstraAlgorithm = new DijkstraAlgorithm(graph);
        List<Edge> edges = dijkstraAlgorithm.getVertexEdges(vertexById);
        graph.getEdges().removeAll(edges);
        /*for (Edge edge : edges){
            graph.getEdges().remove(edge);
        }*/
        graph.getVertexes().remove(vertexById);
    }

    public void deletePlanet(String planetNode) {
        planetDAO.deleteByNode(planetNode);
    }
}
