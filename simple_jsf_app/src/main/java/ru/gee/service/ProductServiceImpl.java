package ru.gee.service;

import ru.gee.persist.CategoryRepository;
import ru.gee.persist.Product;
import ru.gee.persist.ProductRepository;
import ru.geekbrains.service.ProductServiceRemote;
import ru.geekbrains.service.repr.ProductRepr;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Stateless
@Remote(ProductServiceRemote.class)
public class ProductServiceImpl implements ProductService, ProductServiceRemote {

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
    public ProductRepr findById(long id) {
        return createProductReprWithCategory(productRepository.findById(id));
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
