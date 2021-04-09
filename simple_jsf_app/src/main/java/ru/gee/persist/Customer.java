package ru.gee.persist;

import javax.persistence.*;

@Entity
@Table(name = "customer")
@NamedQueries({
        @NamedQuery(name = "deleteCustomerById", query = "delete from Customer c where c.id = :id"),
        @NamedQuery(name = "findAllCustomer", query = "from Customer c"),
        @NamedQuery(name = "count", query = "select count(c) from Customer c")
})
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column
    String name;

    @Column
    String mailAddress;

    public Customer() {
    }

    public Customer(Long id, String name, String mailAddress) {
        this.id = id;
        this.name = name;
        this.mailAddress = mailAddress;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMailAddress() {
        return mailAddress;
    }

    public void setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress;
    }
}
