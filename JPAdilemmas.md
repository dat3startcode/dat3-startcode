## Dates
1. Update them on mysql server and annotate with `@Column(name = "colname", updatable = false)`
2. Use java.time.LocalDateTime to avoid using `@Temporal`
3. Update them on java with :
```java
@PreUpdate
public void onUpdate() {
    editted = LocalDateTime.now();
}

@PrePersist
public void onPersist(){
    editted = LocalDateTime.now();
    created = LocalDateTime.now();
}
```

## Merge
1. When an object has been converted to DTO it might have lost some data (like created_date and updated_date and whatever else is not relevant for the DTO).
2. When converting back to Entity from DTO we might miss some data
3. When merging the entity this data will be removed in db.
4. Solution proposal: 
  1. Let the entity get it self from db and update with fields from DTO. (If the DTO does it, we create a strong coupling from DTO to data logik) Problem Entity needs to have a EMF
  2. Let the EntityFacade do the conversion. It knows the EMF. CONS: the EntityFacade will have to know about DTO
  3. Let the DTOFacade do conversion with private method. CON only that the EMF (that is created here to be send to the EntityFacade) This seems like the best solution!

## ManyToMany
- Allways map associations to a Set (rather than List)
- Provide utility methods for bidirectional associations
```java
public void addBook(Book book) {
    this.books.add(book);
    book.getAuthors().add(this);
}

public void removeBook(Book book) {
    this.books.remove(book);
    book.getAuthors().remove(this);
}
```
1. If you model a bidirectional many-to-many association, you need to make sure to always update both ends of the association. To make that a little bit easier, it’s a general best practice to provide utility methods that add entities to and remove them from the association.
- It seems to be important which side is the owning side. See [here](https://stackoverflow.com/questions/1082095/how-to-remove-entity-with-manytomany-relationship-in-jpa-and-corresponding-join) for more details (Must be verified, first attempt is doubtful). The owning side is the opposite of the one with `mappedBy` (this side is called target or inverse side). 
- On merging with relationships these are not automatically removed if merging an object of the target side.
- Put `@JoinTable` on the owning side:
```java
@JoinTable( // This is now the owner side of the relationsship
    name = "photo_tag",
    joinColumns = @JoinColumn(name = "photo_id"),
    inverseJoinColumns = @JoinColumn(name = "tag_id"))
```
- lazy loading is default, meaning that collections does NOT get loaded unless requested within the scope of the EM. If needed therefore set `@ManyToMany(fetch = FetchType.EAGER)` on the owning side.
- 
