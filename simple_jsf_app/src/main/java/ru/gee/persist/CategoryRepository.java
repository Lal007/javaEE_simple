package ru.gee.persist;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class CategoryRepository {
    @PersistenceContext(unitName = "ds")
    private EntityManager em;

    @TransactionAttribute
    public void save(Category category) {
        if (category.getId() == null) {
            em.persist(category);
        }
        em.merge(category);
    }

    @TransactionAttribute
    public void delete(Long id) {
        em.createNamedQuery("deleteCategoryById")
                .setParameter("id", id)
                .executeUpdate();
    }

    public Category findById(Long id) {
        return em.find(Category.class, id);
    }

    public List<Category> findAll() {
        return em.createNamedQuery("findAllCategory", Category.class).getResultList();
    }

    public long count() {
        return em.createNamedQuery("countCategory", Long.class).getSingleResult();
    }

    public Category getReference(Long id) {
        return em.getReference(Category.class, id);
    }
}
