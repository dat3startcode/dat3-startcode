# Proof of Concept, first time you use the start code

- Clone this project
- Delete the .git folder and Do "Your own" `git init`
- Create your OWN repository for this project on github
- Commit and Push your code to this repository
- Go to *Travis-ci.com* and Sign up with GitHub
- Accept the Authorization of Travis CI. You’ll be redirected to GitHub
- Click the green Activate button, and select the the new repository to be used with Travis CI

- Create two local databases (on your vagrant image) named exactly (exactly is only for this proof of concept) as below:
  - `startcode`
  - `startcode_test`
- Create a REMOTE database on your Droplet named exacly like this: `startcode`
- in a terminal (git bash for Windows Users) in the root of the project type: `mvn test`
- Hopefully the previous step was a success, if not, fix the problem(s)

### Deploy the project (manually) via Maven
- Open the pom-file, and locate the properties-section in the start of the file. Change the value for `remote.server` to the URL for your OWN droplet

- ssh into your droplet and open this file with nano: `/opt/tomcat/bin/setenv.sh`
- add this to the file, with your own values (those you selected [here](https://docs.google.com/document/d/1POXowHvFNSTL6C-QOlivkSnL_iF1ogsLGFRTckbBdt8/edit#heading=h.11opjunivufy)):

`export DEPLOYED="DEV_ON_DIGITAL_OCEAN"`

`export USER="YOUR_DB_USER"`

`export PW="YOUR_DB_PASSWORD"`

`export CONNECTION_STR="jdbc:mysql://localhost:3306/startcode"
`
- Save the file, and restart Tomcat `sudo systemctl restart tomcat`
- Back in a LOCAL terminal (git bash for Windows Users), in the root of the project, type (add your own password):

  `mvn clean test -Dremote.user=script_user -Dremote.password=PW_FOR_script_user tomcat7:deploy`

- If everything was fine the project should be deployed to your droplet, ready to use with the remote database. Test like this:
  - `URL_FOR_YOUR_DROPLET/rest-jpa-devops-starter/api/xxx`  (This does not use the database)
  - `URL_FOR_YOUR_DROPLET/rest-jpa-devops-starter/api/xxx/count` (This queries the database)

### Add CI-to your project and let Maven deploy, when the project BUILDS and tests are GREEN
- Login to travis using Github, and select your project on the dashboard
- Click "More options" and select "settings"
- Create two Environment Variables with names and values as sketched below (must be done in two steps);
  - REMOTE_PW   :  *Your value for the script_user password*
  - REMOTE_USER :  *script_user*
 
 - Now make a small change to index.html (one that is easy to see after deploy)
 - In a terminal, in the root of the project, type: `mvn clean test` (always build and test before you commit)
 - If everything was fine, commit and push your changes
 - On *travis-ci.org* verify that your commit has been detected and a "build cycle" has started
 - If everything was fine (might take a few minutes) verify that Travis has deployed your new war file to your droplet

[Back to README](README.md)
