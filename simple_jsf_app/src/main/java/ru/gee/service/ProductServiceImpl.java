package ru.gee.service;

import ru.gee.persist.Category;
import ru.gee.persist.CategoryRepository;
import ru.gee.persist.Product;
import ru.gee.persist.ProductRepository;
import ru.gee.rest.ProductResource;
import ru.geekbrains.service.ProductServiceRemote;
import ru.geekbrains.service.repr.ProductRepr;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
@Remote(ProductServiceRemote.class)
public class ProductServiceImpl implements ProductService, ProductServiceRemote, ProductResource {

    @EJB
    private ProductRepository productRepository;

    @EJB
    private CategoryRepository categoryRepository;

    @Override
    @TransactionAttribute
    public void save(ProductRepr productRepr) {
        productRepository.save(new Product(productRepr.getId(),
                productRepr.getName(),
                productRepr.getDescription(),
                productRepr.getPrice(),
                categoryRepository.getReference(productRepr.getCategoryId())
        ));
    }

    @Override
    @TransactionAttribute
    public void delete(Long id) {
        productRepository.delete(id);
    }

    @Override
    public void insert(ProductRepr productRepr) {
        if (productRepr.getId() != null) {
            throw new IllegalArgumentException("Not null id in the insert Product method");
        }
        save(productRepr);
    }

    @Override
    public void update(ProductRepr productRepr) {
        if (productRepr.getId() != null) {
            throw new IllegalArgumentException("Null id in the update Product method");
        }
        save(productRepr);
    }

    @Override
    public void insertCategory(Category category) {
        categoryRepository.save(category);
    }

    @Override
    public ProductRepr findById(long id) {
        return createProductReprWithCategory(productRepository.findById(id));
    }

    @Override
    public ProductRepr findByName(String name) {
        Product product = productRepository.findByName(name);
        if (product == null) {
            throw new IllegalArgumentException("No product with name = " + name);
        }

        return createProductReprWithCategory(product);
    }

    @Override
    public List<ProductRepr> findByCategoryId(long categoryId) {
        Category category = categoryRepository.findById(categoryId);
        if (category == null) {
            throw new IllegalArgumentException("No category found with id = " + categoryId);
        }
        return category.getProducts().stream()
                .map(ProductServiceImpl::createProductReprWithCategory)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductRepr> findAll() {
        return productRepository.findAll().stream()
                .map(ProductServiceImpl::createProductRepr)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductRepr> findAllWithCategoryFetch() {
        return productRepository.findAllWithCategoryFetch().stream()
                .map(ProductServiceImpl::createProductReprWithCategory)
                .collect(Collectors.toList());
    }

    @Override
    public long count() {
        return productRepository.count();
    }

    @Override
    public List<ProductRepr> findAllRemote() {
        return findAllWithCategoryFetch();
    }

    public static ProductRepr createProductReprWithCategory(Product product) {
        return new ProductRepr(product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getCategory() != null ? product.getCategory().getId() : null,
                product.getCategory() != null ? product.getCategory().getName() : null
        );
    }

    public static ProductRepr createProductRepr(Product product) {
        return new ProductRepr(product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                null,
                null
        );
    }
}
