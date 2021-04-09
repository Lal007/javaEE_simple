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
import java.math.BigDecimal;
import java.util.List;

@ApplicationScoped
@Named
public class ProductRepository {

    @PersistenceContext(unitName = "ds")
    private EntityManager em;

    @Resource
    private UserTransaction ut;

    @PostConstruct
    public void init() {
        if (count() == 0) {
            try {
                ut.begin();
                save(new Product(null, "Product 1", "Description 1", new BigDecimal(100)));
                save(new Product(null, "Product 2", "Description 2", new BigDecimal(200)));
                save(new Product(null, "Product 3", "Description 3", new BigDecimal(300)));
                save(new Product(null, "Продукт 4", "Description 4", new BigDecimal(300)));
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
    public void save(Product product) {
        if (product.getId() == null) {
            em.persist(product);
        }
        em.merge(product);
    }

    @Transactional
    public void delete(Long id) {
        em.createNamedQuery("deleteProductById")
                .setParameter("id", id)
                .executeUpdate();
    }

    public Product findById(Long id) {
        return em.find(Product.class, id);
    }

    public List<Product> findAll() {
        return em.createNamedQuery("findAllProduct", Product.class).getResultList();
    }

    public long count() {
        return em.createNamedQuery("count", Long.class).getSingleResult();
    }
}
