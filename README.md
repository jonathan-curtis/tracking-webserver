# Tracking Webserver

The project has been built using [Spring Boot](https://spring.io/projects/spring-boot) and [Spring Webflux](https://docs.spring.io/spring-framework/docs/current/reference/html/web-reactive.html).

Webflux was chosen because of the framework's non-blocking nature, 
its ability to scale whilst utilising fewer hardware resources and its concise programming model.

The project's http endpoints can be found in `TrackingWebserverApplication.java`.

## Steps to Run the Project
The project has been created using Java 11 and Gradle. 

The Gradle Wrapper can be used to build and run the application. Ensure that a Java 11 JRE has been configured.

Navigate to the root of the project and execute the following command:

```
./gradlew bootRun
```

Alternatively, you can run the following in the project root:

```
./gradlew clean build
```

Then navigate to the build/libs folder:

```
cd build/libs/
```

And run:

```
java -jar tracking-websever-0.0.1-SNAPSHOT.jar
```

## Interacting with the Service
The service runs on port 8080 by default.
There are two endpoints configured:

```
localhost:8080/img
localhost:8080/ping
```

Calling `/ping` will return a HTTP 200 provided that the file `/tmp/ok` exists on the local filesystem. If it doesn't, 
a 503 will be returned. 

The specific file used to indicate a healthy system can be configured in the `application.properties` file 
with the property `pingFileLocation`. The application will default to `/tmp/ok` if no property is provided.

The `/ping` endpoint returns a 1x1 pixel gif file. This file is loaded from the classpath. The name can be configured in the `application.properties` file with the `pixelFilename` property.
It also logs the following information:
- The [log id](https://docs.spring.io/spring-framework/docs/current/reference/html/web-reactive.html#webflux-logging-id) of the request (because requests can run across multiple threads in Webflux)
- The address of the caller
- The http request method
- The request path
- The status code of the request

## Test Coverage
The project has 95% unit test code coverage.

## Improvements
The logging can be improved by adding additional fields, such as request/response times.