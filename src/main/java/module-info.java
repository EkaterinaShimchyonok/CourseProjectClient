module org.example.courseproject {
    requires java.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires gson;


    opens org.example.courseproject to javafx.fxml;
    exports org.example.courseproject;
    opens org.example.courseproject.POJO to gson;
    opens org.example.courseproject.Controllers to javafx.fxml;
}