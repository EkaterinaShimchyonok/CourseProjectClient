module org.example.courseproject {
    requires java.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires gson;


    opens org.example.courseproject to javafx.fxml;
    exports org.example.courseproject.POJO;
    exports org.example.courseproject;
    opens org.example.courseproject.POJO to gson, javafx.base, javafx.fxml, java.base, javafx.controls, javafx.graphics;
    opens org.example.courseproject.Controllers to javafx.fxml;
    exports org.example.courseproject.Controllers;
}