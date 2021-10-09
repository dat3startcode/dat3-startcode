package dtos;

import entities.Parent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
    public Parent getEntity(){
        Parent p = new Parent(this.name, this.age);
        if(id != 0)
            p.setId(this.id);
        this.children.forEach(child->p.addChild(child.getEntity()));
        return p;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParentDTO parentDTO = (ParentDTO) o;
        return id == parentDTO.id && age == parentDTO.age && Objects.equals(name, parentDTO.name) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, age, children);
    }
}
