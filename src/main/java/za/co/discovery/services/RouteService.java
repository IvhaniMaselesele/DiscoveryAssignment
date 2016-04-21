package za.co.discovery.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.co.discovery.Models.Edge;
import za.co.discovery.Models.Graph;
import za.co.discovery.Models.Route;
import za.co.discovery.Models.Vertex;
import za.co.discovery.dataAccess.RouteDAO;

@Service
public class RouteService {

    RouteDAO routeDAO;

    @Autowired
    public RouteService(RouteDAO routeDAO) {
        this.routeDAO = routeDAO;
    }

    public Edge setSourceAndDestinationById(Graph graph, Edge edge) {

        Vertex sourceVertexById = graph.getVertexById(edge.getSourceId());
        Vertex destinationVertexById = graph.getVertexById(edge.getDestinationId());

        edge.setSource(sourceVertexById);
        edge.setDestination(destinationVertexById);

        return edge;
    }

    public void deleteEdge(Graph graph, Edge edge) {
        graph.getEdges().remove(edge);
    }

    public void deleteRoute(String routeId) {
        routeDAO.deleteByNode(Integer.parseInt(routeId));
    }

    public Route retrieveRouteFromEdge(Edge edge) {
        int id = Integer.parseInt(edge.getEdgeId());
        return routeDAO.retrieve(id);
    }

    public void updateRouteFromEdge(Edge edge) {
        int id = Integer.parseInt(edge.getEdgeId());
        Route route = routeDAO.retrieve(id);
        routeDAO.update(route);
    }

    public Route persistRoute(Route expectedRoute) {
        return routeDAO.save(expectedRoute);
    }
}
