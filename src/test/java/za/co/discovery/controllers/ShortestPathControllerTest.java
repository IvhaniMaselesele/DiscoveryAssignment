package za.co.discovery.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import za.co.discovery.Models.Graph;
import za.co.discovery.dataAccess.PlanetDAO;
import za.co.discovery.dataAccess.RouteDAO;
import za.co.discovery.services.FileReadingService;
import za.co.discovery.services.GraphService;
import za.co.discovery.services.ShortestPathService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(MockitoJUnitRunner.class)
public class ShortestPathControllerTest {
    MockMvc mockMvc;

    @Mock
    PlanetDAO planetDAO;
    @Mock
    RouteDAO routeDAO;

    ShortestPathService shortestPathService;
    FileReadingService fileReadingService;

    @Test
    public void rootReturnsPlanetsPage() throws Exception {
        setupFixture();
        mockMvc.perform(get("/"))
                .andExpect(view().name("planets"));
    }

    @Test
    public void getPlanetsReturnsPlanetsPage() throws Exception {
        GraphService graphService = mock(GraphService.class);
        Graph graph = new Graph();
        shortestPathService = new ShortestPathService();
        fileReadingService = new FileReadingService(planetDAO, routeDAO, shortestPathService);

        FileReadingService spyFileReadingService = spy(fileReadingService);
        setupFixture(graphService, spyFileReadingService);

        when(graphService.createNewGraph()).thenReturn(graph);
        doNothing().when(spyFileReadingService).readRouteAndTrafficSheets(graph);

        mockMvc.perform(get("/getPlanets"))
                .andExpect(view().name("planets"));

    }

    public void setupFixture() {
        shortestPathService = new ShortestPathService();
        FileReadingService fileReadingService = new FileReadingService(planetDAO, routeDAO, shortestPathService);
        mockMvc = standaloneSetup(
                new ShortestPathController(fileReadingService, shortestPathService, new GraphService()))
                .setViewResolvers(getInternalResourceViewResolver())
                .build();
    }

    public void setupFixture(GraphService graphService) {
        shortestPathService = new ShortestPathService();
        fileReadingService = new FileReadingService(planetDAO, routeDAO, shortestPathService);
        mockMvc = standaloneSetup(
                new ShortestPathController(fileReadingService, shortestPathService, graphService))
                .setViewResolvers(getInternalResourceViewResolver())
                .build();
    }

    public void setupFixture(GraphService graphService, FileReadingService fileReadingService) {
        shortestPathService = new ShortestPathService();
        mockMvc = standaloneSetup(
                new ShortestPathController(fileReadingService, shortestPathService, graphService))
                .setViewResolvers(getInternalResourceViewResolver())
                .build();
    }

    private InternalResourceViewResolver getInternalResourceViewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setSuffix(".html");
        return viewResolver;
    }
}