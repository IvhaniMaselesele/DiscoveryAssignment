package za.co.discovery.dataAccess;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import za.co.discovery.Models.Planet;

import static org.hibernate.criterion.Restrictions.eq;

@Repository
@Transactional
public class PlanetDAO {
    private SessionFactory sessionFactory;

    @Autowired
    public PlanetDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Planet save(Planet planet) {
        Session session = sessionFactory.getCurrentSession();
        return (Planet) session.merge(planet);
    }

    public Planet retrieve(String node) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Planet.class);
        criteria.add(eq("node", node));
        return (Planet) criteria.uniqueResult();
    }

    public Planet update(Planet planet) {
        Session session = sessionFactory.getCurrentSession();
        return (Planet) session.merge(planet);
    }

    public void deleteByNode(String node) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("delete Planet where node = :node");
        query.setParameter("node", node);

        query.executeUpdate();
    }
}
