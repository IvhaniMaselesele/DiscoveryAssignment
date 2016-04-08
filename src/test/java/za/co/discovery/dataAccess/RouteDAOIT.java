package za.co.discovery.dataAccess;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;
import za.co.discovery.DaoConfig;
import za.co.discovery.DataSourceConfig;
import za.co.discovery.Models.Route;
import za.co.discovery.PersistenceConfig;

import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;
import static org.hamcrest.CoreMatchers.is;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {PersistenceConfig.class, DataSourceConfig.class, RouteDAO.class,
        DaoConfig.class}, loader = AnnotationConfigContextLoader.class)
public class RouteDAOIT {
    @Autowired
    private SessionFactory sessionFactory;

    private RouteDAO routeDAO;

    @Before
    public void init() throws Exception {
        routeDAO = new RouteDAO(sessionFactory);

    }

    @Test
    public void savedRouteCanBeRetrieved() {
        Route expectedRoute = getRoute(1);
        Session session = sessionFactory.getCurrentSession();

        routeDAO.save(expectedRoute);

        Criteria criteria = session.createCriteria(Route.class);
        Route actualRoute = (Route) criteria.uniqueResult();

        assertThat(actualRoute, is(sameBeanAs(expectedRoute)));
    }

    @Test
    public void savedRouteCanBeUpdated() {
        Route route = getRoute(1);
        Route expectedRoute = new Route(1, "C", "B", 0.2);

        Route savedRoute = routeDAO.save(route);
        Route retrievedRoute = routeDAO.retrieve(savedRoute.getId());
        retrievedRoute.setOrigin("C");

        Route actualRoute = routeDAO.update(retrievedRoute);

        assertThat(actualRoute, is(sameBeanAs(expectedRoute)));
    }

    @Test
    public void deletedRouteCannotBeRetrieved() {
        int id = 1;
        Route route = getRoute(id);

        routeDAO.save(route);
        routeDAO.deleteByNode(id);
        Route deletedRoute = routeDAO.retrieve(id);

        assertThat(deletedRoute, is(sameBeanAs(null)));
    }

    @Test
    public void retrieveRoute_ReturnsSavedRoute() {
        Route expectedRoute = getRoute(2);

        Route savedRoute = routeDAO.save(expectedRoute);
        Route actualRoute = routeDAO.retrieve(savedRoute.getId());

        assertThat(actualRoute, is(sameBeanAs(expectedRoute)));
    }

    private Route getRoute(int id) {
        return new Route(id, "A", "B", 0.2);
    }

}


