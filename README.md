
[![Java CI with Maven](https://github.com/Brest-Java-Course-2021/MaksimukV-football/actions/workflows/maven.yml/badge.svg)](https://github.com/Brest-Java-Course-2021/MaksimukV-football/actions/workflows/maven.yml)

# FOOTBALL CHAMPIONSHIP
This is a simple CRUD Java app example.
## Requirements
- JDK 15
- Apache Maven
## Build application
```
mvn clean install
```
## Run application
Application consists 2 particular modules (web-app & rest-app) that are dependent on each other.
### Start Rest application
To start Rest server (rest-app module):
```
java -jar ./rest-app/target/rest-app-1.0-SNAPSHOT.jar
```
rest-app is set up on http://localhost:8088 (no default redirect provided)
### Start Web application
To start WEB application (web-app module):
```
java -jar ./web-app/target/web-app-1.0-SNAPSHOT.jar
```
web-app is set up on http://localhost:8080 and is listening to :8088 so rest-app must be started already.
### Stop applications
To trigger Rest Shutdown Hook:
```
^C
```
## Rest API
There is an API provided for the rest-app in the project. To access it, go to the
```
http://localhost:8088/swagger-ui.html
```
Note: Rest app should be built and ran for api page to work.
## Model schema

This is how DB tables can be visualized.

![modelschema load error](./documentation/model/model-schema.jpg "Model schema")

## Start up initialization
There are already some entities existing in DB.\
There are 2 teams and 4 players.

