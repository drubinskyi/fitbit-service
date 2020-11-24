# Fitbit service
It is implementation of application which represents a parsing of CSV file with person's daily activity. It stores all records in memory and expose REST API to get data and statistics.

Main technologies were used - Kotlin and Spring Boot for API development, Gradle as build tool.

## Build
Application could be built using appropriate gradle lifecycle commands, for example:
```
./gradlew clean build
```
## Usage
Application is easy to run using following command:
```
./gradlew bootRun
```
#Swagger API documentation
```
http://localhost:8080/swagger-ui.html
```
