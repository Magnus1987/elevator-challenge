# Elevator Coding Challenge

## Prerequisite
It is recommended to run with Java 8 or earlier.

## Build And Run (as is)

As the project is, the Spring app can be started as seen below.

build and run the code with Maven

    mvn package
    mvn spring-boot:run

or start the target JAR file 

    mvn package
    java -jar target/elevator-1.0-SNAPSHOT.jar

## API
Use any of the following calls with i.e. Postman

GET Ping to verify health
http://localhost:8080/elevator/rest/v1/ping

GET List of elevators
http://localhost:8080/elevator/rest/v1/list

POST Request an elevator to floor
http://localhost:8080/elevator/rest/v1/request/{toFloor}

POST Release an elevator given elevators id
http://localhost:8080/elevator/rest/v1/release/{id}