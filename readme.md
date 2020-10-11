# Gruppe60 StudIt App

[![Gitpod Ready-to-Code](https://img.shields.io/badge/Gitpod-Ready--to--Code-blue?logo=gitpod)](https://gitpod.idi.ntnu.no/#https://gitlab.stud.idi.ntnu.no/it1901/groups-2020/gr2060/gr2060)


## Project structure
The project is seperated into two modules:
* Application logic is under *studit/core/..*  
* GUI implementations is under *studi/fxui/..*
---
Main code for each module is found under *src/main/..*  
Testing for each module is found under *src/test/..*  

## Installation and testing
The project uses maven for building and running.

* To build the project, click the gitpod link and run: `mvn install` in the root folder, to run all the testes and quality checking.

* To run the project, you have to use the fxmui-module and therefore run `mvn javafx:run -f fxui/pom.xml`

* For unit testing and code coverage, run `mvn test`  

To login once the application is started, enter *user* as username and *password* as password

Code coverage is found under *target/site/jacoco*

## User Stories

- As a coming student I want to find information about a subject I am going to take at NTNU, so that I can be prepared when I start to study
- As a student I want to find information about different subjects at NTNU, so I can compare subjects I consider to choose
- As a student I want to ask questions about a subject I am taking at NTNU, that I want to know the answer to immediately


