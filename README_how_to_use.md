# How to use this start project for future rest/jpa project with CI

### Initial Setup
- Clone this project
- Delete the .git folder and Do "Your own" `git init`
- Create your OWN repository for this project on github
- Commit and Push your code to this repository
- Go to *Travis-ci.com* and Sign up with GitHub
- Accept the Authorization of Travis CI. You’ll be redirected to GitHub
- Click the green Activate button, and select the the new repository to be used with Travis CI

- Create two local databases to be used for local development (replace xxx with a name that makes sense for your project):
  - `xxx`
  - `xxx_test`
- *(We suggest you always follow the naming pattern used above (xxx and xxx_test) for your databases)*
- Create a REMOTE database on your Droplet with the same name as your dev-database (xxx above)
- **Important:** Locate the file **.travis.yml** in the root, and change the script `CREATE DATABASE startcode_test;`to use the SAME name as your local test-database (xxx_test) above
- in a terminal (git bash for Windows Users) in the root of the project type: `mvn test`
- Hopefully the previous step was a success, if not, fix the problem(s)

### Add your own features
- Delete or rename (refactor)/change the RenameMe.java entity Class (if you refactor, remember to change the named query)
- Delete or rename (refactor)/change the Facade to reflect your new changes
- Delete or rename (refactor)/change the RenameMeResource. **Important:** Remember to change the database name, and observe the new way of getting an EntityManagerFactory
- Delete or rename the two test files. **Important:** Remember to change the database names to the test-database you created for this project 
- After all this, build and run the tests before you continue.

### Deploy the project (manually) via Maven
- Open the pom-file, and locate the properties-section in the start of the file. Change the value for `remote.server` to the URL for your OWN droplet

- ssh into your droplet and open this file with nano: `/opt/tomcat/bin/setenv.sh`
- add this to the file, using your own values (those you selected [here](https://docs.google.com/document/d/1POXowHvFNSTL6C-QOlivkSnL_iF1ogsLGFRTckbBdt8/edit#heading=h.11opjunivufy))
(*This might have been added already, if you have completed the "getting stated guide"*):

`export DEPLOYED="DEV_ON_DIGITAL_OCEAN"`

`export USER="YOUR_DB_USER"`

`export PW="YOUR_DB_PASSWORD"`

`export CONNECTION_STR="jdbc:mysql://localhost:3306/NAME_OF_YOUR_DB"
`
- Save the file, and restart Tomcat `sudo systemctl restart tomcat`
- Back in a LOCAL terminal (git bash for Windows Users), in the root of the project, type (add your own password):

  `mvn clean test -Dremote.user=script_user -Dremote.password=PW_FOR_script_user tomcat7:deploy`

- If everything was fine the project should be deployed to your droplet, ready to use with the remote database. Verify via some of the GET-endpoints you have added to your code


### ADD CI-control to your project and let Maven deploy, when the project BUILDS and all tests are GREEN
- Login to Travis using Github, and select your project on the dashboard
- Click "More options" and select "settings"
- Create two Environment Variables with names and values as sketched below (must be done in two steps);
 - REMOTE_PW   :  *Your value for the script_user password*
 - REMOTE_USER :  *script_user*
 
 - Now make a small change to index.html (one that is easy to see after deploy)
 - In a terminal, in the root of the project, type: `mvn clean test` (**always build and test before you commit**)
 - If everything was fine, commit and push your changes
 - On *travis-ci.org* verify that your commit has been detected and a "build cycle" has started
 - If everything was fine (might take a few minutes) verify that Travis has deployed your new war file to your droplet

[Back to README](README.md)

