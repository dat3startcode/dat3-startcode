package dtos;

import entities.Child;
import entities.Parent;

import java.util.ArrayList;
import java.util.List;

public class ChildDTO {
    private int id;
    private String name;
    private int age;
    private List<ToyDTO> toys = new ArrayList<>();

    public ChildDTO(Child child) {
        if(child.getId()!=0)
            this.id = child.getId();
        this.name = child.getName();
        this.age = child.getAge();
        child.getToys().forEach(toy->this.toys.add(new ToyDTO(toy)));
    }
    public Child getEntity(){
        Child c = new Child(this.name, this.age);
        this.toys.forEach(toy->c.addToy(toy.getEntity()));
        return c;
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

    public List<ToyDTO> getToys() {
        return toys;
    }

}
