package za.co.discovery.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import za.co.discovery.Models.Planet;
import za.co.discovery.Models.Route;
import za.co.discovery.dataAccess.PlanetDAO;
import za.co.discovery.dataAccess.RouteDAO;

import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FileReadingServiceTest {
    FileReadingService fileReadingService;
    @Mock
    PlanetDAO planetDAO;
    @Mock
    RouteDAO routeDAO;

    @Before
    public void init() {
        fileReadingService = new FileReadingService(planetDAO, routeDAO, new ShortestPathService());
    }

    @Test
    public void persistPlanetShouldSavePlanet_AndReturnResult() {
        //TODO : use a builder
        Planet expectedPlanet = new Planet("random node", "random name");
        when(planetDAO.save(expectedPlanet)).thenReturn(expectedPlanet);
        Planet actualPlanet = fileReadingService.persistPlanet(expectedPlanet);
        assertThat(actualPlanet, is(sameBeanAs(expectedPlanet)));
    }

    @Test
    public void persistRouteShouldSaveRoute_AndReturnResult() {
        Route expectedRoute = new Route(1, "random origin", "random destination", 0.2);
        when(routeDAO.save(expectedRoute)).thenReturn(expectedRoute);
        Route actualRoute = fileReadingService.persistRoute(expectedRoute);
        assertThat(actualRoute, is(sameBeanAs(expectedRoute)));
    }


}