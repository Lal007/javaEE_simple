package ru.gee.rest;

import ru.gee.persist.Category;
import ru.geekbrains.service.repr.ProductRepr;

import javax.ejb.Local;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Local
@Path("/v1/product")
public interface ProductResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    List<ProductRepr> findAll();

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    ProductRepr findById(@PathParam("id") long id);

    @GET
    @Path("byName/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    ProductRepr findByName(@PathParam("name") String name);

    @GET
    @Path("byCategory/{categoryId}")
    @Produces(MediaType.APPLICATION_JSON)
    List<ProductRepr> findByCategoryId(@PathParam("categoryId") long categoryId);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    void insert(ProductRepr productRepr);

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    void update(ProductRepr productRepr);

    @POST
    @Path("newCategory")
    @Consumes(MediaType.APPLICATION_JSON)
    void insertCategory(Category category);

    @DELETE
    @Path("/{id}")
    void delete(@PathParam("id") Long id);
}
