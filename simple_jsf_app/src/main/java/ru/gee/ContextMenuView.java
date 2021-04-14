package ru.gee;

import ru.gee.persist.Product;
import ru.gee.persist.ProductRepository;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named("dtContextMenuView")
@ViewScoped
public class ContextMenuView implements Serializable {

    private List<Product> products;

    private Product selectedProduct;

    @Inject
    private ProductRepository productRepository;

    public void preloadData() {
        products = productRepository.findAll();
    }

    public Product getSelectedProduct() {
        return selectedProduct;
    }

    public void setSelectedProduct(Product selectedProduct) {
        this.selectedProduct = selectedProduct;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void deleteProduct() {
        productRepository.delete(selectedProduct.getId());
        selectedProduct = null;
    }
}
