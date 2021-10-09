package dtos;

import entities.Parent;
import entities.Toy;

import java.util.ArrayList;
import java.util.List;

public class ToyDTO {
    private int id;
    private String name;
    private int age;
    private List<Integer> children = new ArrayList<>(); //Children are changed to their ids (IN ORDER TO BREAK THE CIRCULAR REFERENCE between child and toy)

    public ToyDTO(Toy toy) {
        if (toy.getId() != 0)
            this.id = toy.getId();
        this.name = toy.getName();
        this.age = toy.getAge();
        toy.getChildren().forEach(child->this.children.add(child.getId()));
    }
    public Toy getEntity(){
        return new Toy(this.name, this.age);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<Integer> getChildren() {
        return children;
    }
}
