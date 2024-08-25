module edu.neumont.csc180 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;


    opens edu.neumont.csc180.controller to javafx.fxml;
    exports edu.neumont.csc180;
    opens edu.neumont.csc180.view to javafx.fxml;
}