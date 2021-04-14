package ru.gee.controller;

import ru.gee.service.CartService;
import ru.geekbrains.service.repr.ProductRepr;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@SessionScoped
@Named
public class CartController implements Serializable {

    @EJB
    private CartService cartService;

    public void add(ProductRepr productRepr) {
        cartService.add(productRepr);
    }
}
