package ru.gee.persist;

import javax.persistence.*;

@Entity
@Table(name = "category")
@NamedQueries({
        @NamedQuery(name = "deleteCategoryById", query = "delete from Category c where c.id = :id"),
        @NamedQuery(name = "findAllCategory", query = "from Category c"),
        @NamedQuery(name = "countCategory", query = "select count(c) from Category c")
})
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    public Category() {
    }

    public Category(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
