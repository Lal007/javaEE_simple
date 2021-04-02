package ru.gee;

import ru.gee.persist.Customer;
import ru.gee.persist.CustomerRepository;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet(urlPatterns = "/customer/*")
public class CustomerControllerServlet extends HttpServlet {
    private CustomerRepository customerRepository;

    private static final Pattern pathParam = Pattern.compile("\\/(\\d*)$");


    @Override
    public void init() throws ServletException {
        customerRepository = (CustomerRepository) getServletContext().getAttribute("customerRepository");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getPathInfo() == null || req.getPathInfo().isEmpty() || req.getPathInfo().equals("/")) {
            req.setAttribute("customers", customerRepository.findAll());
            getServletContext().getRequestDispatcher("/WEB-INF/views/customer.jsp").forward(req, resp);
        } else if (req.getPathInfo().equals("/new")) {
            req.setAttribute("customer", new Customer());
            getServletContext().getRequestDispatcher("/WEB-INF/views/new_customer_form.jsp").forward(req, resp);
        } else {
            Matcher matcher = pathParam.matcher(req.getPathInfo());
            if (matcher.matches()) {
                long id;
                try {
                    id = Long.parseLong(matcher.group(1));
                } catch (NumberFormatException e) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }
                Customer customer = customerRepository.findById(id);
                if (customer == null) {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                }
                req.setAttribute("customer", customer);
                getServletContext().getRequestDispatcher("/WEB-INF/views/customer_form.jsp").forward(req, resp);
            }
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getPathInfo() == null || req.getPathInfo().isEmpty() || req.getPathInfo().equals("/")) {
            String strId = req.getParameter("id");

            try {
                Customer product = new Customer(
                        strId == null ? null : Long.parseLong(strId),
                        req.getParameter("name"),
                        req.getParameter("mailAddress"));
                customerRepository.save(product);
                resp.sendRedirect(getServletContext().getContextPath() + "/customer");
            } catch (NumberFormatException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
