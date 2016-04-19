package za.co.discovery.controllers;

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
import za.co.discovery.services.FileReadingService;
import za.co.discovery.services.GraphService;
import za.co.discovery.services.PlanetService;
import za.co.discovery.services.ShortestPathService;

import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;
import static org.hamcrest.CoreMatchers.is;
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

    @Test
    public void rootReturnsPlanetsPage() throws Exception {
        setupFixture();
        mockMvc.perform(get("/"))
                .andExpect(view().name("index"));
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
        mockMvc.perform(get("/getRoutes"))
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
                .andExpect(model().attributeExists("routeToEdit"));
    }

    @Test
    public void editRoutePost_ShouldRoute() throws Exception {
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



    public void setupFixture() {
        graphService = mock(GraphService.class);
        shortestPathService = new ShortestPathService();
        graph = new Graph();

        //FileReadingService fileReadingService = new FileReadingService(planetDAO, routeDAO, shortestPathService);
        //FileReadingService spyFileReadingService = spy(fileReadingService);
        fileReadingService = mock(FileReadingService.class);
        planetService = new PlanetService(planetDAO);

        when(graphService.createNewGraph()).thenReturn(graph);
        //doNothing().when(spyFileReadingService).readRouteAndTrafficSheets(graph);


        mockMvc = standaloneSetup(
                new ShortestPathController(fileReadingService, shortestPathService, graphService, planetService))
                .setViewResolvers(getInternalResourceViewResolver())
                .build();
    }

    public void setupFixture(GraphService graphService) {
        shortestPathService = new ShortestPathService();
        fileReadingService = new FileReadingService(planetDAO, routeDAO, shortestPathService);
        planetService = new PlanetService(planetDAO);
        mockMvc = standaloneSetup(
                new ShortestPathController(fileReadingService, shortestPathService, graphService, planetService))
                .setViewResolvers(getInternalResourceViewResolver())
                .build();
    }

    public void setupFixture(GraphService graphService, FileReadingService fileReadingService) {
        shortestPathService = new ShortestPathService();
        mockMvc = standaloneSetup(
                new ShortestPathController(fileReadingService, shortestPathService, graphService, planetService))
                .setViewResolvers(getInternalResourceViewResolver())
                .build();
    }

    private InternalResourceViewResolver getInternalResourceViewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setSuffix(".html");
        return viewResolver;
    }
}