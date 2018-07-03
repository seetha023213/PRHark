# PRHark - It is a Spring boot application . Comes with an in-built Tomcat webserver.
Running from command prompt: prerequisite: maven should be installed.
1. Download the project from git. 
2. Run the following commands i and ii:
    i.  cd "Project location"
    ii. mvn package
    
 Eg: cd "c:\users\seetha\git\prhark" followed by command: mvn package
This creates a jar file in your project target directory.
Run the jar using: java -jar target/prhawk-0.0.1-SNAPSHOT.jar
this should run the application. Access the application using http://localhost:8080/user/{userName}

Or you can run the application from Eclipse/any java IDE
pre requisites: jdk 1.8 , Eclipse/Any java IDE ,maven(if IDE doesnot come with one)
1. Run as maven install
2. run the application.java file(it has main method(Spring boot) which loads the application with an inbuilt tomcat)
