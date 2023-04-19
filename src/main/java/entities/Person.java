package entities;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "person")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private String name;
    private int age;

    @ManyToMany
    @JoinTable(
            name="Person_hobby",
            joinColumns=@JoinColumn(name="Person_id", referencedColumnName="id"),
            inverseJoinColumns=@JoinColumn(name="hobby_id", referencedColumnName="id"))
    private List<Hobby> hobbies;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public Person() {
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

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    public List<Hobby> getHobbies() {
        return hobbies;
    }

    public void addHobby(Hobby hobby) {
        this.hobbies.add(hobby);
        hobby.addPerson(this);
    }

    public void removeHobby(Hobby hobby) {
        this.hobbies.remove(hobby);
        hobby.removePerson(this);
    }
}