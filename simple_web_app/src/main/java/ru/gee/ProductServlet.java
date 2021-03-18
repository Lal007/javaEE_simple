package ru.gee;

import ru.gee.persist.Product;
import ru.gee.persist.ProductRepository;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/product/*")
public class ProductServlet extends HttpServlet {

    private ProductRepository productRepository;

    @Override
    public void init() throws ServletException {
        productRepository = (ProductRepository) getServletContext().getAttribute("productRepository");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getPathInfo() == null) {
            resp.getWriter().println("<table>");
            resp.getWriter().println("<tr>");
            resp.getWriter().println("<th>Id</th>");
            resp.getWriter().println("<th>Name</th>");
            resp.getWriter().println("<th>Description</th>");
            resp.getWriter().println("<th>Price</th>");
            resp.getWriter().println("</tr>");

            for (Product product : productRepository.findAll()) {
                resp.getWriter().println("<tr>");
                resp.getWriter()
                        .println("<td><a href='" + getServletContext().getContextPath()
                                + req.getServletPath() + "/" + product.getId() + "'>" + product.getId() + "</a></td>");
                resp.getWriter().println("<td>" + product.getName() + "</td>");
                resp.getWriter().println("<td>" + product.getDescription() + "</td>");
                resp.getWriter().println("<td>" + product.getPrice() + "</td>");
                resp.getWriter().println("</tr>");
            }
            resp.getWriter().println("</table>");
        } else {
            String idString = req.getPathInfo().substring(1);
            long id;
            try {
                id = Long.parseLong(idString);
            } catch (NumberFormatException e) {
                resp.getWriter().println("Wrong product id: " + idString);
                return;
            }
            Product product = productRepository.findById(id);

            if (product != null) {
                resp.getWriter().println("<p>Product info</p>");
                resp.getWriter().println("<p>Name: " + product.getName() + "</p>");
                resp.getWriter().println("<p>Description: " + product.getDescription() + "</p>");
                resp.getWriter().println("<p>Price: " + product.getPrice() + "</p>");
            } else {
                resp.getWriter().println("<p>No such product</p>");
            }
        }
    }
}
