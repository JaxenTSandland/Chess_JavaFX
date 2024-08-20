module personal.sandland.jaxen.jaxens_chessjavafx {
    requires javafx.controls;
    requires javafx.fxml;


    opens personal.sandland.jaxen.jaxens_chessjavafx to javafx.fxml;
    exports personal.sandland.jaxen.jaxens_chessjavafx;
}