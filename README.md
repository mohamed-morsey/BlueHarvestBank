# Blue Harvest Banking Application
Simple banking application for Blue Harvest coding challenge.

## Instructions to Build and Start the BlueHarvestBank Application

## Prerequisites
1. JDK 1.8.
2. Maven 3.

## Building the BlueHarvestBank Application
1. Checkout the project code from GitHub.
2. Go to project folder.
3. Run command **"mvn clean install"** to build the application including unit and integration tests.

## Starting up the Application
1. Go to project folder.
2. Run command **"cd bank"**.
3. Run command **"mvn spring-boot:run"** to start the application.

The application starts and it is available at **"http://localhost:8080"**.

## Supported Operating Systems
The application has been tested on:

1. Ubuntu 16.04 LTS.
2. Windows 7 Ultimate.

## Supported Browsers
The application has been tested on:

1. Mozilla Firefox.
2. Google Chrome.
3. Opera.
4. Microsoft Internet Explorer.

## Design and Implementation Details
The application is a web based application developed based on Spring Boot framework.
Currently, the application supports managing customers, accounts and transactions.
However, the application can be extended easily to support more entities, e.g. credit cards.

The application has the following features:

1. **Extensibility**: A generic interface called *"CrudService"* that incorporates the basic CRUD (create, read, update, 
and delete) operations. This interface is implemented by *"CustomerService"* and *"AccountService"* 
in order to support CRUD operations for customers and accounts. If a new entity is added and needs to be supported
e.g. credit card, a service is created for this entity and should implement that interface.
2. **Model-View-Controller(MVC)**: The application adheres to the MVC pattern via Spring framework.
3. **Separation of Concerns (SoC)**: The application achieves separation of concerns (SoC) via applying 
Data Transfer Object (DTO) pattern. For instance, for class *"Customer"* a DTO class called *"CustomerDto"*
is created.
4. **Database**: H2 database is used in embedded mode to provide data storage.
5. **Data Integrity**: The application executes transactions for critical operations
that require to be performed as a single unit of work, i.e. in case of establishing a new
account with initial credit, an entry for this transaction must be add to *Transaction* table. 
6. **Data Validation**: The application uses validators to check and validate input data, e.g. *"CustomerValidator"* 
and *"AccountValidator"*.
7. **Exception Handling**: The application defines an exception handler, called *"BankExceptionHandler*", which catches the exceptions thrown 
and returns the appropriate HTTP status code along with an error message.
8. **Testing**: Unit and integration tests are used to cover the various application features.
9. **Code Quality**: The code quality is inspected and checked with [SonarQube](https://sonarcloud.io/about/sq).
10. **Frontend**: The application has a very simple UI developed using [Thymeleaf](https://www.thymeleaf.org/).

## Design Objectives
The main objectives of this design are:

1. **Scalability**: We are using H2 database in embedded mode but it is quite simple to port it to 
another database system running in client-server mode.
2. **Extensibility**: Minor changes are required to support more entities, e.g. credit cards.
3. **Robustness**: Using unit and integration tests to cover as many test scenarios as possible
insures the robustness of the system.
4. **Data Integrity**: Data integrity is maintained via using transactions in the critical 
operations that require such behavior.