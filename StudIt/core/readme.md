# Core-module (core)
This directory consists of the domain- and persistence layer for the studIt.

## Domain-layer
The domain layer contains all classes and logic related to the data that the application is about and handles. This layer is completely independent of the user interface. 
Our app is about courses at NTNU and a chatbot which is supposed to simplify subject choice. The domain layer contains classes to represent and manage such, and these can be found in the studit.core package.

## Persistence-layer
The persistence-layer consists of all the classes and logic related to writing and reading (storage) of the data in the domain-layer. Our persistence-layer implements file storage with JSON-syntax. And this layer can be found in the studit.json-package.


## Building with maven
This is an important module and the because the domain- and persistence-layer work as a class-library for other modules in studit. In addition, we use various code quality analysis tools (jacoco with jacoco, spotbugs with com.github.spotbugs and checkstyle with checkstyle). These are set up so that they do not stop the building unless everything is in order.
