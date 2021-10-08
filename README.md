
# Rest snippets


#### ChildrenFacadeTest and ParentFacadeTest
- No CascadeTypes are used
- Parent is OneToMany with Child
- Child is ManyToMany with Toy
- These 5 methods are tested for both relation types
  - getById
  - getAll
  - create
  - update
  - delete
- Tests verify that NO DUPLICATES are created
- Tests verify that related entities are persisted or merged with related entities (both known and unknown in db)

