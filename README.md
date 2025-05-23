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
through the use of Spring Boot's 
[docker compose support](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.docker-compose).

Alternatively, and more akin to how you would run the application in production, you can run the application with java overriding
the `spring.datasource.*`, `spring.activemq.*` and `*.otlp.` properties to point at an already running Postgres database, 
ActiveMQ instance and OpenTelemety endpoint respectively. The example below does this through the use of environment variables to
point at a Postgres database etc, running in Docker.

```shell
$ SPRING_DATASOURCE_URL=jdbc:postgresql://127.0.0.1:5432/spt-development-demo \
  SPRING_DATASOURCE_USERNAME=postgres \
  SPRING_DATASOURCE_PASSWORD=p@ssw0rd \
  SPRING_ACTIVEMQ_BROKER_URL=tcp://localhost:61616 \
  MANAGEMENT_OTLP_METRICS_EXPORT_URL=http://localhost:4318/v1/metrics \
  OTEL_EXPORTER_OTLP_LOGS_ENDPOINT=http://localhost:4318/v1/logs \
  OTEL_EXPORTER_OTLP_METRICS_ENDPOINT=http://localhost:4318/v1/metrics \
  OTEL_EXPORTER_OTLP_TRACES_ENDPOINT=http://localhost:4318/v1/traces \
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
    --data '{"id":4, "title":"My Book - updated","blurb":"My blurb - updated","author":"Me","rrp":1000}' \
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
Additionally, the Actuator web endpoints have been enabled on port 8081 and can be accessed unauthorized. For example:

```shell
$ curl -v http://localhost:8081/actuator/health
```
or
```shell
$ curl -v http://localhost:8081/actuator/info
```

Running the demo in docker
==========================

There are multiple ways to [build a docker image](https://www.baeldung.com/spring-boot-docker-images) for Spring Boot 
applications. The simplest way is to use Buildpacks.

```shell
$ ./mvnw spring-boot:build-image
```
The [docker-compose.service.yml](./docker-compose.service.yml) can then be used to run the image along with 
[docker-compose.yml](./docker-compose.yml) to start up Postgres and ActiveMQ.

```shell
$ docker compose -f docker-compose.yml -f docker-compose.service.yml up -d
```
The cURL commands above can again be used to test the API.

Grafana
-------

Spring Boot 3.4.0 extended the Docker Compose support to support 
[Grafana LGTM](https://grafana.com/blog/2024/03/13/an-opentelemetry-backend-in-a-docker-image-introducing-grafana/otel-lgtm/).

This project's Docker Compose files have been updated to include Grafana LGTM and whether running the demo with the Spring Boot
Maven plugin or with Docker Compose, Grafana can be accessed [here](http://localhost:3000/). The application will send metrics,
logs and traces to Grafana which has been provisioned with the following dashboards for visualising this data:

* JVM Overview
* RED Metrics
* Spring Boot 3.x Statistics
* Spring Boot Observability

**NOTE** As stated on the Grafana LGTM page, Grafana LGTM is not production ready and "is an open source backend for OpenTelemetry
that’s intended for development, demo, and testing environments."