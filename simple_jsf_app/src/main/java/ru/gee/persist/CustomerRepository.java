package ru.gee.persist;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.SystemException;
import javax.transaction.Transactional;
import javax.transaction.UserTransaction;
import java.util.List;

@ApplicationScoped
@Named
public class CustomerRepository {
    @PersistenceContext(unitName = "ds")
    private EntityManager em;

    @Resource
    private UserTransaction ut;

    @PostConstruct
    public void init() {
        if (count() == 0) {
            try {
                ut.begin();
                save(new Customer(null, "Customer 1", "Mail 1"));
                save(new Customer(null, "Customer 2", "Mail 2"));
                save(new Customer(null, "Customer 3", "Mail 3"));
                ut.commit();
            } catch (Exception e) {
                try {
                    ut.rollback();
                } catch (SystemException systemException) {
                    throw new RuntimeException(systemException);
                }
                throw new RuntimeException(e);
            }
        }
    }

    @Transactional
    public void save(Customer customer) {
        if (customer.getId() == null) {
            em.persist(customer);
        }
        em.merge(customer);
    }

    @Transactional
    public void delete(Long id) {
        em.createNamedQuery("deleteCustomerById")
                .setParameter("id", id)
                .executeUpdate();
    }

    public Customer findById(Long id) {
        return em.find(Customer.class, id);
    }

    public List<Customer> findAll() {
        return em.createNamedQuery("findAllCustomer", Customer.class).getResultList();
    }

    public long count() {
        return em.createNamedQuery("countCustomer", Long.class).getSingleResult();
    }
}
