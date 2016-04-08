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
import za.co.discovery.Models.Planet;
import za.co.discovery.PersistenceConfig;

import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;
import static org.hamcrest.CoreMatchers.is;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {PersistenceConfig.class, DataSourceConfig.class, PlanetDAO.class,
        DaoConfig.class}, loader = AnnotationConfigContextLoader.class)
public class PlanetDaoIT {
    @Autowired
    private SessionFactory sessionFactory;

    private PlanetDAO planetDAO;

    @Before
    public void init() {
        planetDAO = new PlanetDAO(sessionFactory);
    }

    @Test
    public void savedPlanetCanBeRetrieved() {
        Planet expectedPlanet = getPlanet("A");
        Session session = sessionFactory.getCurrentSession();

        planetDAO.save(expectedPlanet);

        Criteria criteria = session.createCriteria(Planet.class);
        Planet actualPlanet = (Planet) criteria.uniqueResult();

        assertThat(actualPlanet, is(sameBeanAs(expectedPlanet)));
    }

    @Test
    public void savedPlanetCanBeUpdated() {
        Planet planet = getPlanet("A");
        Planet expectedPlanet = new Planet("A", "new name");

        Planet savedPlanet = planetDAO.save(planet);
        Planet retrievedPlanet = planetDAO.retrieve(savedPlanet.getNode());
        retrievedPlanet.setName("new name");

        Planet actualPlanet = planetDAO.update(retrievedPlanet);

        assertThat(actualPlanet, is(sameBeanAs(expectedPlanet)));
    }

    @Test
    public void deletedPlanetCannotBeRetrieved() {
        String node = "A";
        Planet planet = getPlanet(node);

        planetDAO.save(planet);
        planetDAO.deleteByNode(node);
        Planet deletedPlanet = planetDAO.retrieve(node);

        assertThat(deletedPlanet, is(sameBeanAs(null)));
    }

    @Test
    public void retrievePlanet_ReturnsSavedPlanet() {
        Planet expectedPlanet = getPlanet("B");

        Planet savedPlanet = planetDAO.save(expectedPlanet);
        Planet actualPlanet = planetDAO.retrieve(savedPlanet.getNode());

        assertThat(actualPlanet, is(sameBeanAs(expectedPlanet)));
    }

    private Planet getPlanet(String node) {
        return new Planet(node, "AB");
    }
}