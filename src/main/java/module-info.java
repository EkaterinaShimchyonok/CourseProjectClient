module org.example.courseproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires com.fasterxml.jackson.datatype.jdk8;
    requires java.desktop;

    opens org.example.courseproject to javafx.fxml;
    exports org.example.courseproject.POJO;
    exports org.example.courseproject;
    opens org.example.courseproject.POJO to javafx.base, javafx.fxml, com.fasterxml.jackson.databind;
    opens org.example.courseproject.Controllers to javafx.fxml;
    exports org.example.courseproject.Controllers;
}
