package za.co.discovery.services;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import za.co.discovery.Models.Graph;
import za.co.discovery.Models.Planet;
import za.co.discovery.Models.Vertex;
import za.co.discovery.dataAccess.PlanetDAO;

import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PlanetServiceTest {

    PlanetService planetService;
    @Mock
    private PlanetDAO planetDAO;

    @Before
    public void init() {
        planetService = new PlanetService(planetDAO);
    }

    @Test
    public void updateVertex_ShouldUpdateVertex() {
        String planetName = "random name";
        String node = "random node";
        Planet planet = new Planet(node, planetName);
        String vertexName = "updated random name";
        String vertexId = planet.getNode();


        Vertex expectedVertex = new Vertex(vertexId, vertexName);

        Graph graph = new Graph();
        planetService.addPlanetToGraphAsVertex(graph, planet);

        planet.setName(vertexName);
        Vertex actualVertex = planetService.updateVertex(graph, planet);
        assertThat(actualVertex, is(sameBeanAs(expectedVertex)));
    }

    @Test
    public void addPlanetToGraphAsVertex_ShouldAddNewVertex() {
        String planetName = "random name";
        String node = "random node";
        Planet planet = new Planet(node, planetName);
        String vertexName = planet.getName();
        String vertexId = planet.getNode();

        Vertex expectedVertex = new Vertex(vertexId, vertexName);

        Graph graph = new Graph();
        Vertex actualVertex = planetService.addPlanetToGraphAsVertex(graph, planet);

        assertThat(actualVertex, is(sameBeanAs(expectedVertex)));
    }

    @Test
    public void persistPlanetShouldSavePlanet_AndReturnResult() throws Exception {
        //TODO : use a builder
        Planet expectedPlanet = new Planet("random node", "random name");

        // expectations
        when(planetDAO.save(expectedPlanet)).thenReturn(expectedPlanet);

        Planet actualPlanet = planetService.persistPlanet(expectedPlanet);
        assertThat(actualPlanet, CoreMatchers.is(sameBeanAs(expectedPlanet)));
    }

    @Test
    public void createNewPlanetReturnsPlanet() throws Exception {
        String name = "random name";
        String node = "random node";
        Planet expectedPlanet = new Planet(node, name);
        Planet actualPlanet = planetService.createNewPlanet(node, name);
        assertThat(actualPlanet, is(sameBeanAs(expectedPlanet)));

    }
}