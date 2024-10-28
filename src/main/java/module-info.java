module org.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;


    requires java.xml.bind;
    requires java.xml;


    opens org.example.XMLController to java.xml.bind;
    opens org.example.Model.Entity to java.xml.bind;
    opens org.example to javafx.fxml;
    opens org.example.View to javafx.fxml;


    exports org.example;
    exports org.example.View;
    exports org.example.Model.Entity;
}
