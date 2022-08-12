package entities;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneId;

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

    // @Temporal(TemporalType.TIMESTAMP) //Not necessary to annotate with @Temporal when using java 8 time.LocalDateTime (translates to TIMESTAMP on mysql
    @Column(name = "created", updatable = false)
    // @UpdateTimestamp //this update the timestamp everytime the entity is changed
    private LocalDateTime created;

    // @Temporal(TemporalType.TIMESTAMP) //Not necessary when using java.time.LoalDateTime
    @Column(name = "modified")//, updatable = false) //We have to make it updatable since it is not handles on database level. PROBLEM: DTO does not contain dates so when photo entities return from the user, their dates are null.
    private LocalDateTime editted;

    // Database is not set to handle dates, so I do it with JPA lifecycle methods. For more see: https://www.baeldung.com/jpa-entity-lifecycle-events
    @PreUpdate
    public void onUpdate() {
        editted = LocalDateTime.now(ZoneId.of("GMT+02:00"));
    }

    @PrePersist
    public void onPersist(){
        editted = LocalDateTime.now(ZoneId.of("GMT+02:00"));
        created = LocalDateTime.now(ZoneId.of("GMT+02:00"));
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
