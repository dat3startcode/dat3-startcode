package entities;

import javax.persistence.*;
import java.util.*;

@Entity
@NamedQuery(name = "Tool.deleteAllRows", query = "DELETE from Tool")
public class Tool {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique=true)
    private String name;
    private Integer age;
    private Double price;

    //Cascading between Tool and Toy is a BAD idea (since both has their own identity (they can exist meaningfully without each other)). Problems can be seen in ToolFacadeTest
    @ManyToMany(mappedBy = "tools", cascade = CascadeType.ALL) //NEVER USE CascadeType.ALL here because NEVER USE CascadeType.REMOVE on ManyToMany (and especially BAD using it on both sides of the relationship)
    private Set<Toy> toys;

    public Tool() {}

    public Tool(String name, Integer age, Double price) {
        this.name = name;
        this.age = age;
        this.price = price;
        this.toys = new HashSet();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name", unique = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "age")
    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Set<Toy> getToys() {
        return toys;
    }

    public void setToys(Set<Toy> toys) {
        this.toys = toys;
    }

    public void addToy(Toy t) {
        this.toys.add(t);
        if(!t.getTools().contains(this))
            t.getTools().add(this);
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tool toy = (Tool) o;
        return id == toy.id && Objects.equals(name, toy.name) && Objects.equals(age, toy.age);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, age);
    }
}
