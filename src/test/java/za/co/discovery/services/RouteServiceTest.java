package za.co.discovery.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import za.co.discovery.Models.Edge;
import za.co.discovery.Models.Graph;
import za.co.discovery.Models.Route;
import za.co.discovery.Models.Vertex;
import za.co.discovery.dataAccess.RouteDAO;

import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RouteServiceTest {

    RouteService routeService;
    @Mock
    private RouteDAO routeDAO;

    @Before
    public void init() {
        routeService = new RouteService(routeDAO);
    }

    @Test
    public void updateVertex_ShouldUpdateVertex() {

    }

    @Test
    public void setSourceBySourceId_ShouldReturnEdge_WithSourceVertex() throws Exception {
        Graph graph = new Graph();
        //TODO : use builder
        Edge edge = new Edge();
        Vertex destination = new Vertex("random destination Id", "1");
        Vertex source = new Vertex("random source Id", "2");
        Edge expectedEdge = new Edge("", source, destination, 2);
        edge.setSourceId("random source Id");
        edge.setDestinationId("random destination Id");

        expectedEdge.setSourceId("random source Id");
        expectedEdge.setDestinationId("random destination Id");
        graph.addVertex(source);
        graph.addVertex(destination);

        Edge actualEdge = routeService.setSourceAndDestinationById(graph, edge);

        assertThat(actualEdge, is(sameBeanAs(expectedEdge).ignoring("weight").ignoring("edgeId")));

    }

    @Test
    public void retrieveRouteFromEdge_ShouldReturnARoute() throws Exception {
        Route expectedRoute = new Route(1, "", "", 2);
        Vertex source = new Vertex();
        Vertex destination = new Vertex();
        Edge edge = new Edge("1", source, destination, 2);

        when(routeDAO.retrieve(1)).thenReturn(expectedRoute);

        Route actualRoute = routeService.retrieveRouteFromEdge(edge);
        assertThat(actualRoute, sameBeanAs(expectedRoute));
    }

    @Test
    public void persistRouteShouldSaveRoute_AndReturnResult() throws Exception {
        //TODO : use a builder
        Route expectedRoute = new Route(1, "", "", 2);

        // expectations
        when(routeDAO.save(expectedRoute)).thenReturn(expectedRoute);

        Route actualPlanet = routeService.persistRoute(expectedRoute);
        assertThat(actualPlanet, sameBeanAs(expectedRoute));
    }

    @Test
    public void deleteEdge_ShouldRemoveEdgeFromEdgesList() throws Exception {
        Graph graph = new Graph();
        Edge edge = new Edge();
        edge.setEdgeId("random edge id");
        graph.addEdge(edge);

        Graph expectedGraph = new Graph();
        assertThat(graph, is(not(sameBeanAs(expectedGraph))));
        routeService.deleteEdge(graph, edge);

        assertThat(graph, is(sameBeanAs(expectedGraph)));
    }
}