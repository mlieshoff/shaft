# Shaft - Smart Measurement API

Dockerized Spring-Boot Rest Application to measure data. 

## Prerequisites

For running the application:

* Docker 24.0.5

For developing:

* Maven 3.9.5
* Java >= 20

## Setup

You can set up the *server.port* in the *application.properties*. The default is *8080*. 

If the port is changed please also change the *SERVICE_PORT_EXPOSE* in the *.env* file for the *shaft* service.

You can also change the database port via the property *MARIADB_PORT_EXPOSE*. If you did, please change also the 
*spring.datasource.url* property according the port in the *application.properties* files.

Also, you can change the directory where the application stores its data via the property *DATA_DIR* in the *.env* file.

## Run the application

Open a console and go to the project dir. Then execute following commands to start the application:

``docker-compose build``
``docker-compose up -d``

## Stop the application

Open a console and go to the project dir. Then execute following command to stop the application:

``docker-compose down``

## Open API docs

The rest API of the application is documented on the following link which can be opened in a browser:

*localhost:8080/swagger-ui/index.html*

A open API compatible schema can be downloaded by GETting the link:

*http://localhost:8080/api-docs*

Note: please change the port when developing to *8088*

## Logs

Logs can be displayed with use of the following command:

For the database:

``docker logs shaft-maria-db``

For the application:

``docker logs shaft-service``

## Development

To start the application for developing please make sure you use the *application-dev.properties*. 
Like in the following command:

``
java -jar target/shaft.jar -Dspring.config.locationEntity=src/main/resources/application-dev.properties
``

## Automated Tests

The application is tested completely automatically with executing a *mvn clean package*

There are different kind of tests:

* unit tests (JUnit)
* (database) integration tests (DBUnit / Testcontainers)
* cloud contract tests (Spring CDC / Testcontainers)
