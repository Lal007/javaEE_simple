package ru.geekbrains.service;

import ru.geekbrains.service.repr.ProductRepr;

import java.util.List;

public interface ProductServiceRemote {

    List<ProductRepr> findAllRemote();
}
