package entities;

import javax.persistence.*;

@Entity
@NamedQuery(name = "IdentificationCard.deleteAllRows", query = "DELETE from IdentificationCard")
public class IdentificationCard {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private IdentificationType type;
    private String number;
    private String description;
    @ManyToOne
    private Parent parent;

    public IdentificationCard() {}

    public IdentificationCard( IdentificationType type, String number, String description) {
        this.type = type;
        this.number = number;
        this.description = description;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public IdentificationType getType() {
        return type;
    }

    public void setType(IdentificationType type) {
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setParent(Parent parent) {
        this.parent = parent;
    }

    public Parent getParent() {
        return parent;
    }

    public enum IdentificationType {
        DRIVERS_LICENS,
        PASSPORT,
        SOCIAL_SECURITY_CARD
    }
}
