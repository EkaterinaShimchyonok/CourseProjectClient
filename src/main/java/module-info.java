module org.example.courseproject {
    requires java.base;
    requires javafx.controls;
    requires javafx.fxml;




    opens org.example.courseproject to javafx.fxml;
    exports org.example.courseproject;
}