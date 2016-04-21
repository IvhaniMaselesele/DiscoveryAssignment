package za.co.discovery.controllers;

import org.hamcrest.CustomMatcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import za.co.discovery.Models.Edge;
import za.co.discovery.Models.Graph;
import za.co.discovery.Models.Vertex;
import za.co.discovery.dataAccess.PlanetDAO;
import za.co.discovery.dataAccess.RouteDAO;
import za.co.discovery.services.*;

import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(MockitoJUnitRunner.class)
public class ShortestPathControllerTest {
    MockMvc mockMvc;

    @Mock
    PlanetDAO planetDAO;
    @Mock
    RouteDAO routeDAO;
    Graph graph;
    GraphService graphService;
    PlanetService planetService;

    ShortestPathService shortestPathService;
    FileReadingService fileReadingService;
    RouteService routeService;

    @Test
    public void rootReturnsPlanetsPage() throws Exception {
        setupFixture();
        mockMvc.perform(get("/"))
                .andExpect(view().name("index"));
    }

    @Test
    public void planetsList_ReturnsPlanetsListPage() throws Exception {
        setupFixture();
        mockMvc.perform(get("/planetsList"))
                .andExpect(view().name("planetsList"));
        // TODO : test that model exists
    }

    @Test
    public void getPlanetsReturnsPlanetsPage() throws Exception {
        setupFixture();
        mockMvc.perform(get("/getPlanets"))
                .andExpect(view().name("planets"));
        // TODO : test that model exists
    }

    @Test
    public void editPlanetGet_ShouldReturnEditPlanetPage() throws Exception {
        setupFixture();
        String randomVertexId = "random vertexId";
        String randomVertexName = "random vertex name";
        Vertex vertex = new Vertex(randomVertexId, randomVertexName);

        graph.addVertex(vertex);

        mockMvc.perform(get("/editPlanetPage/" + randomVertexId))
                .andExpect(status().isOk())
                .andExpect(view().name("editPlanet"))
                .andExpect(model().attributeExists("vertexToEdit"));
    }

    @Test
    public void editPlanetPost_ShouldUpdateVertex() throws Exception {
        setupFixture();
        String randomVertexId = "random vertex id";
        String randomVertexName = "random vertex name";
        String newRandomVertexName = "new random vertex name";

        Vertex vertex = new Vertex(randomVertexId, randomVertexName);
        Vertex expectedVertex = new Vertex(randomVertexId, newRandomVertexName);

        graph.addVertex(vertex);
        mockMvc.perform(post("/editPlanetPageSubmit").param("id", randomVertexId).param("name", newRandomVertexName))
                .andExpect(view().name("planetsList"))
                .andExpect(model().attributeExists("planets"));

        Vertex actualVertex = graph.getVertexById(randomVertexId);
        assertThat(actualVertex, is(sameBeanAs(expectedVertex)));
    }

    @Test
    public void deletePlanet_ShouldReturnPlanetsListPage() throws Exception {
        setupFixture();
        String randomVertexName = "random vertex name";
        String randomVertexId = "random vertex id";
        Vertex vertex = new Vertex(randomVertexId, randomVertexName);

        graph.addVertex(vertex);

        mockMvc.perform(delete("/deletePlanet/" + randomVertexId))
                .andExpect(view().name("planetsList"))
                .andExpect(model().attributeExists("planets"));

    }

    @Test
    public void addPlanetGet_ShouldReturnCreateNewPlanetPage() throws Exception {
        setupFixture();

        mockMvc.perform(get("/addPlanetPage"))
                .andExpect(status().isOk())
                .andExpect(view().name("createPlanet"))
                .andExpect(model().attributeExists("newVertex"));
    }

    @Test
    public void addPlanetPostPage_ShouldReturnPlanetsListPage() throws Exception {
        setupFixture();
        String randomVertexId = "random vertex id";
        String randomVertexName = "random vertex name";
        Vertex expectedVertex = new Vertex(randomVertexId, randomVertexName);

        mockMvc.perform(post("/addPlanetPageSubmit").param("name", randomVertexName))
                .andExpect(view().name("planetsList"))
                .andExpect(model().attributeExists("planets"));

        Vertex actualVertex = graph.getVertexes().get(0);

        assertThat(actualVertex, is(sameBeanAs(expectedVertex).ignoring("id")));
    }

    @Test
    public void getRoutesReturnsRoutesPage() throws Exception {
        setupFixture();
        mockMvc.perform(get("/routesList"))
                .andExpect(view().name("routesList"))
                .andExpect(model().attributeExists("routes"));
        // TODO : test that model exists
    }

    @Test
    public void editRouteGet_ShouldReturnEditRoutePage() throws Exception {
        setupFixture();
        Vertex sourceVertex = new Vertex("source id", "source id");
        Vertex destinationVertex = new Vertex("destination id", "destination name");

        graph.addVertex(sourceVertex);
        graph.addVertex(destinationVertex);
        //TODO : use a builder
        double weight = 0.2;
        String randomEdgeId = "random edge id";
        Edge edge = new Edge(randomEdgeId, sourceVertex, destinationVertex, weight);
        graph.addEdge(edge);

        mockMvc.perform(get("/editRoutePage/" + randomEdgeId))
                .andExpect(status().isOk())
                .andExpect(view().name("editRoute"))
                .andExpect(model().attributeExists("edge"));
    }

    @Test
    public void editRoutePost_ShouldRoute() throws Exception {
        RouteService routeService = mock(RouteService.class);

        setupFixture(routeService);

        String randomSourceId = "A";
        String randomDestinationId = "B";
        String newRandomDestinationId = "C";
        Vertex destinationVertex = new Vertex(randomDestinationId, "B");
        Vertex newDestinationVertex = new Vertex(newRandomDestinationId, "C");
        Vertex sourceVertex = new Vertex(randomSourceId, "A");

        //TODO : use a builder
        Edge edge2 = new Edge();
        edge2.setSource(sourceVertex);
        edge2.setDestination(newDestinationVertex);
        edge2.setDestinationId(newRandomDestinationId);
        edge2.setSourceId(randomSourceId);

        Edge edge = new Edge();
        edge.setSource(sourceVertex);
        edge.setDestination(destinationVertex);
        edge.setDestinationId(randomDestinationId);
        edge.setSourceId(randomSourceId);
        edge.setWeight(2);
        edge.setEdgeId("1");
        graph.addEdge(edge);

        when(routeService.setSourceAndDestinationById(eq(graph), argThat(hasRoute(randomSourceId, newRandomDestinationId)))).thenReturn(edge2);

        mockMvc.perform(post("/editRoutePageSubmit")
                .param("edgeId", edge.getEdgeId())
                .param("sourceId", randomSourceId)
                .param("destinationId", newRandomDestinationId)
                .param("weight", "2"))
                .andExpect(view().name("routesList"))
                .andExpect(model().attributeExists("routes"));

        Edge actualEdge = graph.getEdges().get(0);
        assertThat(actualEdge, sameBeanAs(edge2));
    }

    @Test
    public void addRouteGet_ShouldReturnCreateNewRoutePage() throws Exception {
        setupFixture();

        mockMvc.perform(get("/addRoutePage"))
                .andExpect(status().isOk())
                .andExpect(view().name("createRoute"))
                .andExpect(model().attributeExists("newEdge"))
                .andExpect(model().attributeExists("planets"));
    }

    @Test
    public void addRoutePostPage_ShouldReturnRouteListPage_WithNewRouteAdded() throws Exception {
        RouteService routeService = mock(RouteService.class);

        setupFixture(routeService);

        String randomSourceId = "A";
        String randomDestinationId = "B";
        Vertex destinationVertex = new Vertex(randomDestinationId, "B");
        Vertex sourceVertex = new Vertex(randomSourceId, "A");


        Edge edge2 = new Edge();
        edge2.setSource(sourceVertex);
        edge2.setDestination(destinationVertex);
        edge2.setDestinationId(randomDestinationId);
        edge2.setSourceId(randomSourceId);

        Edge edge = new Edge();
        edge.setDestinationId(randomDestinationId);
        edge.setSourceId(randomSourceId);
        edge.setWeight(2);

        when(routeService.setSourceAndDestinationById(eq(graph), argThat(hasRoute(randomSourceId, randomDestinationId)))).thenReturn(edge2);

        mockMvc.perform(post("/addRoutePageSubmit")
                .param("sourceId", randomSourceId)
                .param("destinationId", randomDestinationId)
                .param("weight", "2"))
                .andExpect(view().name("routesList"))
                .andExpect(model().attributeExists("routes"));

        Edge actualEdge = graph.getEdges().get(0);
        assertThat(actualEdge, sameBeanAs(edge2));
    }

    @Test
    public void deleteRoute_ShouldReturnRouteListPage() throws Exception {
        setupFixture();
        Edge edge = new Edge();
        String randomEdgeId = "1";
        edge.setEdgeId(randomEdgeId);
        graph.addEdge(edge);

        mockMvc.perform(delete("/deleteRoute/" + edge.edgeId))
                .andExpect(view().name("routesList"))
                .andExpect(model().attributeExists("routes"));
    }

    private HasRoute hasRoute(String randomSourceId, String randomDestinationId) {
        return new HasRoute(randomSourceId, randomDestinationId);
    }

    private class HasRoute extends CustomMatcher<Edge> {

        private String sourceId;
        private String destinationId;

        public HasRoute(String sourceId, String destinationId) {
            super("Source and destination matches");
            this.sourceId = sourceId;
            this.destinationId = destinationId;
        }

        @Override
        public boolean matches(Object o) {
            Edge edge = (Edge) o;
            return edge.sourceId.equals(sourceId) && edge.destinationId.equals(destinationId);
        }
    }

    public void setupFixture() {
        graphService = mock(GraphService.class);
        shortestPathService = new ShortestPathService();
        graph = new Graph();
        fileReadingService = mock(FileReadingService.class);
        planetService = new PlanetService(planetDAO);
        routeService = new RouteService(routeDAO);

        when(graphService.createNewGraph()).thenReturn(graph);

        mockMvc = standaloneSetup(
                new ShortestPathController(fileReadingService, shortestPathService, graphService, planetService, routeService))
                .setViewResolvers(getInternalResourceViewResolver())
                .build();
    }

    public void setupFixture(RouteService routeService) {
        graphService = mock(GraphService.class);
        shortestPathService = new ShortestPathService();
        graph = new Graph();
        fileReadingService = mock(FileReadingService.class);
        planetService = new PlanetService(planetDAO);

        when(graphService.createNewGraph()).thenReturn(graph);

        mockMvc = standaloneSetup(
                new ShortestPathController(fileReadingService, shortestPathService, graphService, planetService, routeService))
                .setViewResolvers(getInternalResourceViewResolver())
                .build();
    }

    public void setupFixture(GraphService graphService) {
        shortestPathService = new ShortestPathService();
        fileReadingService = new FileReadingService(planetDAO, routeDAO, shortestPathService);
        planetService = new PlanetService(planetDAO);
        routeService = new RouteService(routeDAO);
        mockMvc = standaloneSetup(
                new ShortestPathController(fileReadingService, shortestPathService, graphService, planetService, routeService))
                .setViewResolvers(getInternalResourceViewResolver())
                .build();
    }

    public void setupFixture(GraphService graphService, FileReadingService fileReadingService) {
        shortestPathService = new ShortestPathService();
        routeService = new RouteService(routeDAO);
        mockMvc = standaloneSetup(
                new ShortestPathController(fileReadingService, shortestPathService, graphService, planetService, routeService))
                .setViewResolvers(getInternalResourceViewResolver())
                .build();
    }

    private InternalResourceViewResolver getInternalResourceViewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setSuffix(".html");
        return viewResolver;
    }
}