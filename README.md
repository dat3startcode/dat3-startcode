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
databasename-test

First time you use this code, observe how persistence.xml contains no properties
