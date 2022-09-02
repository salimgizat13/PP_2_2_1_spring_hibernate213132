package hiber.dao;

import hiber.model.Car;
import hiber.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class UserDaoImp implements UserDao {

   @Autowired
   private SessionFactory sessionFactory;

   @Override
   public void add(User user) {
      sessionFactory.getCurrentSession().save(user);
   }

   @Override
   public List <User> getUsersByCarModelAndSeries(String model, int series) {
      Transaction transaction = null;
      List<User> list = null;
      try (Session session = sessionFactory.openSession()) {
         transaction = session.beginTransaction();
         Query query = session.createQuery("FROM User as u INNER JOIN FETCH u.car where u.car.model = :model and u.car.series = :series ");
         query.setParameter("model", model);
         query.setParameter("series", series);
         list = query.getResultList();
         transaction.commit();
      } catch (Exception e) {
         if (transaction != null) {
            transaction.rollback();
         }
      }
      return list;
   }


   @Override
   @SuppressWarnings("unchecked")
   public List<User> listUsers() {
      TypedQuery<User> query=sessionFactory.getCurrentSession().createQuery("from User");
      return query.getResultList();
   }

}
