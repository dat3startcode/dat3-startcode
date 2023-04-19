package dtos;

import entities.Person;

import java.util.List;
import java.util.stream.Collectors;

public class PersonDTO {
    private Long id;
    private String name;
    private int age;
    private List<HobbyDTO> hobbies;
    public PersonDTO(Person p) {
        this.id = p.getId();
        this.name = p.getName();
        this.age = p.getAge();
        if(p.getHobbies() != null)
            this.hobbies = p.getHobbies().stream().map(h -> new HobbyDTO(h)).collect(Collectors.toList());
    }

    public static List<PersonDTO> getDtos(List<Person> persons) {
        return persons.stream().map(p -> new PersonDTO(p)).collect(Collectors.toList());
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

    public List<HobbyDTO> getHobbies() {
        return hobbies;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
