package za.co.discovery.dataAccess;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import za.co.discovery.Models.Route;

import static org.hibernate.criterion.Restrictions.eq;

@Repository
@Transactional
public class RouteDAO {

    private SessionFactory sessionFactory;

    @Autowired
    public RouteDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Route save(Route route) {
        Session session = sessionFactory.getCurrentSession();
        return (Route) session.merge(route);
    }

    public Route retrieve(int id) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Route.class);
        criteria.add(eq("id", id));
        return (Route) criteria.uniqueResult();
    }

    public Route update(Route route) {
        Session session = sessionFactory.getCurrentSession();
        return (Route) session.merge(route);
    }

    public void deleteByNode(int id) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("delete Route where id = :id");
        query.setParameter("id", id);

        query.executeUpdate();
    }
}
