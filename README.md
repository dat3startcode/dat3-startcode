
# JPA snippets
[Data Model can be found here](https://docs.google.com/document/d/1YrsGp67ODMlHHbDClwyhpK8TGJNa-SSCszltG6kHtQY/edit?usp=sharing)

### TODO: 
1. Create example with call to other method that merges the object. Then em.find and see the entity hasnt changed. (Like viewno and reorderAll in photoviewer)
2. Create example with @ManyToMany duplicate entry ... for key. Even when using Set instead of list on both sides. (Because of using new operator or persist instead of merge)
3. @ManyToMany update list if it is changed on depending side.

### Setup in Intellij
- open view->too windows->persistence
- open the Database tab and create a new data source (remember to point to a database even though this is already written in the persistence unit. This is necessary in order to use the Persistence window)
- in the persistence window right click the pu or an entity and choose "console"
- write a jpql query in the console and execute it.
### In netbeans
- just right click the pu and choose: "Run JPQL query"

### Create model in workbench (cannot be done from Intellij - No model designer yet)
- file-> new model
- dobbelclick the mydb icon and change to relevant database (create one first if needed)
- click the Add Diagram icon
- click the table icon in the left side panel and click in the squared area to insert new table
- dobbelclick the new table and change name and add columns (remember to add a check mark in 'ai' for the primary key)
- do the process again to add a second table
- now in the panel choose the 'non identifying relationship' on to many
- click first on the child table (the one that should hold the foreign key) and then on the parent. A new relationship was now added.
- When done with designing - goto top menu: Database->forward engineer.
  - Check that all settings looks right and click continue
  - click continue again (no changes needed here)
  - Make sure the 'Export mysql table objects' is checked and Show filter to make sure that all your tables are in the 'objects to process' window -> click continue
  - Verify that the generated script looks right -> click continue
  - click close and open the database to see the new tables, that was just created.

### create entities from database in Intellij (Persistence mappings)
- From inside the Persistence window:
- Right-click a persistence unit, point to Generate Persistence Mapping and select By Database Schema.
- Select the 
  - data source 
  - package
  - tick tables to include
  - open tables to see columns and add the ones with mapped type: Collection<SomeEntity> and SomeEntity
  - click OK.
- Tidy up
  - add under @id annotation: @GeneratedValue(strategy = GenerationType.IDENTITY)
  - optionally add under @Entity: @NamedQuery(name = "RenameMe.deleteAllRows", query = "DELETE from RenameMe") and change RenameMe to the Class name.
  - add no-args default constructors
  - add any convenient constructors for your entity
  - add getters and setters for all fields
### In netbeans
- Right click project name -> new -> persistence -> Entity classes From Database -> choose database connection from list -> add the tables you need -> Finish

### Setup new test class in Intellij
- Put carret in the class name line -> alt+enter -> create Test
- Choose which methods to test
- Copy paste From FacadeExampleTest all the @Before and After methods and change code inside to the right Entity type
- Create a System.out.println in each of the test method telling what is being tested
- Create 2 variable initializations
  - expected
  - actual
- Make the assertion
- Create several tests for each method where different things can happen like:
  - Test for thrown exception
  - Test for entity update with new relationships
  - Test for entity update with detached objects

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

