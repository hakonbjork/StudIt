module studit {
  requires transitive com.fasterxml.jackson.core;
  requires transitive com.fasterxml.jackson.databind;

  requires javafx.fxml;
  requires transitive javafx.graphics;
  requires javafx.controls;
  requires com.fasterxml.jackson.annotation;
  requires guava;

  exports studit.core;

  opens studit.core.chatbot to com.fasterxml.jackson.databind;
}
