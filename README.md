# PRHawk - Is a Spring boot application . Comes with an in-built Tomcat webserver.
Ways to execute -
1) Running from command prompt: 
Prerequisite: maven, JDK 1.8 should be installed.
Instructions to execute from command prompt -
a. Download the project from github. 
b. Run the below commands:
    i.  cd "location where you have downloaded the git project"
    ii. mvn package
    
Eg: cd "c:\users\seetha\git\prhawk" followed by command: mvn package
This creates a jar file in your project target directory.
Run the jar using: java -jar target/prhawk-0.0.1-SNAPSHOT.jar
this should run the application. Access the application using http://localhost:8080/user/{userName}

2) Run the application from Eclipse/any java IDE
Prerequisite: JDK 1.8 , Eclipse/Any java IDE ,maven(if IDE does not come with one)
a. Run as maven install
b. Run the application.java file(it has main method(Spring boot) which loads the application with an inbuilt tomcat)

Before running the application please update the github credentials in the application.properties file located at src/main/resources. Below are the credential properties -
github.userName=
github.password=
