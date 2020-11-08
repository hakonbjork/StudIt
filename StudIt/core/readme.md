# Core-module (core)
This directory consists of the domain- and persistence layer for the studIt.

## Domain-layer
Domenelaget inneholder alle klasser og logikk knyttet til dataene som applikasjonen handler om og håndterer. Dette laget skal være helt uavhengig av brukergrensesnittet eller lagingsteknikken som brukes.
Vår app handler om samlinger av såkalte geo-lokasjoner, altså steder identifisert med lengde- og breddegrader. Domenelaget inneholder klasser for å representere og håndtere slike, og disse finnes i simpleex.core-pakken.

## Persistence-layer
The persistence-layer consists of all the classes and logic related to writing and reading (storage) of the data in the domain-layer. Our persistence-layer implements file storage with JSON-syntax. And this layer can be found in the studit.json-package.


## Building with maven
This is an important module and the because the domain- and persistence-layer work as a class-library for other modules in studit. In addition, we use various code quality analysis tools (jacoco with jacoco, spotbugs with com.github.spotbugs and checkstyle with checkstyle). These are set up so that they do not stop the building unless everything is in order.
