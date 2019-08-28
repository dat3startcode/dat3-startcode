[![Build Status](https://travis-ci.org/dat3startcode/rest-jpa-devops-startcode.svg?branch=master)](https://travis-ci.org/dat3startcode/rest-jpa-devops-startcode)

# Getting Started

## Proof of Concept, first time you use the start code

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

### Now lets deploy the project (manually) via Maven
- Open the pom-file, and locate the properties-section in the start of the file. Change the value for `remote.server` to the URL for your OWN droplet

- ssh into your droplet and open this file with nano: `/opt/tomcat/bin/setenv.sh`
- add this to the file, with your own values:

`export DEPLOYED="DEV_ON_DIGITAL_OCEAN"`

`export USER="YOUR_DB_USER"`

`export PW="YOUR_DB_PASSWORD"`

`export CONNECTION_STR="jdbc:mysql://localhost:3306/startcode"
`
- Save the file, and restart Tomcat `sudo systemcctl restart tomcat`
- Back in a LOCAL terminal (git bash for Windows Users) in the root of the project type:

  `mvn clean -Dremote.user=script_user -Dremote.password=lyngby tomcat7:deploy`

- If everything was fine the project should be deployed to your droplet, ready to use with the remote database. Test like this:
  - `URL_FOR_YOUR_DROPLET/rest-jpa-devops-starter/api/xxx`  (This does not use the database)
  - `URL_FOR_YOUR_DROPLET/rest-jpa-devops-starter/api/xxx/count (This queries the database)






Click the green Activate button, and select the repositories you want to use with Travis CI

klon projekt
delete the .git folder 
Do your own git init
Create a repository for this project and commmit it to git "initial commit"
Go to Travis and "flip the switch"

## Make a small change WITHOUT BREAKING ANYTHING and veryfy that 


Lav to databaser (dev og test)

Change this line in the .travis.yml file to use the SAME name as used for your test database in the previous step:
sudo mysql -u root -e "CREATE DATABASE mydb_test;"


We suggest you always follow this pattern for namming your databases
databasename
databasename_test

First time you use this code, observe how persistence.xml contains no properties
