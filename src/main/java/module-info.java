module org.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml.bind;

    opens org.example to javafx.fxml;
    exports org.example;
}
