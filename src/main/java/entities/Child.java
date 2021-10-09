package entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NamedQuery(name = "Child.deleteAllRows", query = "DELETE from Child")
public class Child {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private Integer age;
    @ManyToOne
    @JoinColumn(name = "parent_id", referencedColumnName = "id") //, nullable = false)
    private Parent parent;

    @ManyToMany(mappedBy = "children") //, cascade = CascadeType.PERSIST) //, fetch = FetchType.EAGER) //Target side of relationsship (inverse side)
    private List<Toy> toys;

    public Child() { }

    public Child(String name, Integer age) {
        this.name = name;
        this.age = age;
        this.toys = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name")
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Child child = (Child) o;

        if (id != child.id) return false;
        if (name != null ? !name.equals(child.name) : child.name != null) return false;
        if (age != null ? !age.equals(child.age) : child.age != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (age != null ? age.hashCode() : 0);
        return result;
    }

    public Parent getParent() {
        return parent;
    }

    public void setParent(Parent parent) {
        this.parent = parent;
    }

    public List<Toy> getToys() {
        return toys;
    }

    public void setToys(List<Toy> toys) {
        this.toys = toys;
    }

    public void addToy(Toy toy) {
        this.toys.add(toy);
        if(!toy.getChildren().contains(this)){
            toy.addChildren(this);
        }
    }
}
