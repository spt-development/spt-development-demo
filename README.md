````
  ____  ____ _____   ____                 _                                  _   
 / ___||  _ \_   _| |  _ \  _____   _____| | ___  _ __  _ __ ___   ___ _ __ | |_ 
 \___ \| |_) || |   | | | |/ _ \ \ / / _ \ |/ _ \| '_ \| '_ ` _ \ / _ \ '_ \| __|
  ___) |  __/ | |   | |_| |  __/\ V /  __/ | (_) | |_) | | | | | |  __/ | | | |_ 
 |____/|_|    |_|   |____/ \___| \_/ \___|_|\___/| .__/|_| |_| |_|\___|_| |_|\__|
                                                 |_|                                           
 demo----------------------------------------------------------------------------
````

[![build_status](https://github.com/spt-development/spt-development-demo/actions/workflows/build.yml/badge.svg)](https://github.com/spt-development/spt-development-demo/actions)

A simple demo project demonstrating how to integrate the following open source projects into a Spring Boot application,
through the use of the corresponding Spring Boot starters.

* [spt-development/spt-development-audit-spring](https://github.com/spt-development/spt-development-audit-spring)
* [spt-development/spt-development-cid-jms-spring](https://github.com/spt-development/spt-development-cid-jms-spring)
* [spt-development/spt-development-cid-web](https://github.com/spt-development/spt-development-cid-web)
* [spt-development/spt-development-logging-spring](https://github.com/spt-development/spt-development-logging-spring)

The project provides a simple 'books' REST API backed by a postgres database (see below), that shows how the use of these 
projects can be used to quickly and easily add production grade logging to your Spring Boot projects. The project also 
integrates [spt-development/spt-development-audit-spring](https://github.com/spt-development/spt-development-audit-spring)
demonstrating how to use simple annotations to capture audit information. The auditing is configured to use the 
recommended approach of writing the audit records to a JMS queue and processing them asynchronously.

Building locally
================

To build the project and run the integration tests, run the following Maven command:

```shell
$ ./mvnw clean install
```

The integration tests use [Testcontainers](https://www.testcontainers.org/) therefore docker must be installed on the
machine the build is run on.

**NOTE** To update `mvnw` run `mvn wrapper:wrapper`.

Running the demo
================

The best way to understand how things are working is to run and debug the integration tests in your favourite IDE. However, 
to run the demo project from the command line, the easiest way is to use the Spring Boot plugin (the project currently requires
JDK 17 or above).

```shell
$ ./mvnw spring-boot:run
```

This will also use the [docker-compose.yml](./docker-compose.yml) file to start up postgres and ActiveMQ in docker containers
through the user of Spring Boot's 
[docker compose support](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.docker-compose).

Alternatively, and more akin to how you would run the application in production, you can run the application with java overriding the
`spring.datasource.*` and `spring.activemq.*` properties to point at an already running postgres database. The example below does this 
through the use of environment variables to point at a postgres database running in Docker.

```shell
$ SPRING_DATASOURCE_URL=jdbc:postgresql://127.0.0.1:59314/spt-recruitment-demo \
  SPRING_DATASOURCE_USERNAME=postgres SPRING_DATASOURCE_PASSWORD=p@ssw0rd \
  SPRING_ACTIVEMQ_BROKER_URL=tcp://localhost:59313 \
  java -jar target/spt-development-demo-0.0.1-SNAPSHOT.jar 
```

Once the application is running, the REST API can then be exercised with cURL as follows:

```shell
$ curl -v -u bob:password123! --header "Content-Type: application/json" \
    --request POST \
    --data '{"title":"My Book","blurb":"My blurb","author":"Me","rrp":1000}' \
    http://localhost:8080/api/v1.0/books
```
```shell
$ curl -v -u bob:password123! --header "Content-Type: application/json" \
    --request PUT \
    --data '{"id":44, "title":"My Book - updated","blurb":"My blurb - updated","author":"Me","rrp":1000}' \
    http://localhost:8080/api/v1.0/books/4
```
```shell
$ curl -v -u bob:password123! http://localhost:8080/api/v1.0/books
```
```shell
$ curl -v -u bob:password123! http://localhost:8080/api/v1.0/books/4
```
```shell
$ curl -v -u bob:password123! -X DELETE http://localhost:8080/api/v1.0/books/4
```