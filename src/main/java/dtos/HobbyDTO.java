package dtos;

import entities.Hobby;

import java.util.List;
import java.util.stream.Collectors;

public class HobbyDTO {
    private Long id;
    private String name;
    private String description;
    private List<String> persons;
    public HobbyDTO(Hobby h) {
        this.id = h.getId();
        this.name = h.getName();
        this.description = h.getDescription();
        if(h.getPersons() != null)
            this.persons = h.getPersons().stream().map(p -> p.getName()).collect(Collectors.toList());
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public List<String> getPersons() {
        return persons;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
}
