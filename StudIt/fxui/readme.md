## UI-module
The UI-module consists of all classes and logic related to the visualization and actions on the data from the domain-layer. Our UI shows a login screen, a list of selected courses from NTNU, a chatbot, a detailed page about each course and a discussion forum related to each course.
JavaFX and FXML are used for the UI and is founder the studit.ui-package. The JavaFX-code in src/main/java and the FXML-files in src/main/resources).

## Building with maven
Our maven-build is set up with add-ons for java applications in general (application) and with JavaFX in particular (org.openjfx.javafxplugin). 
In addition, we use various code quality analysis tools (jacoco with jacoco, spotbugs with com.github.spotbugs and checkstyle with checkstyle). These are set up so that they do not stop the building unless everything is in order.