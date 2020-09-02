# Elevator Coding Challenge

There are some design choices to this solution that is good to know and that can be improved.

* It is possible to request several elevators to the same floor. This may clog the queue as we dont have any request timeout. On the other hand it may be sufficient to hold the elevator for some situations.

* It is possible to adjust the speed and polling interval through configuration.

* For now we fail silently if an error would occur such as trying to moving elevator out of bounds. This could be improved by throwing BAD_REQUEST-exceptions.

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