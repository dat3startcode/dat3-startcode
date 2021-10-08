package dtos;

import entities.Parent;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ParentDTO {
    private int id;
    private String name;
    private int age;
    private List<ChildDTO> children = new ArrayList<>();

    public ParentDTO(Parent parent) {
        if(parent.getId()!=0)
            this.id = parent.getId();
        this.name = parent.getName();
        this.age = parent.getAge();
        parent.getChildren().forEach(child->this.children.add(new ChildDTO(child)));
    }
    public static List<ParentDTO> toList(List<Parent> parents) {
        return parents.stream().map(ParentDTO::new).collect(Collectors.toList());
    }
}
