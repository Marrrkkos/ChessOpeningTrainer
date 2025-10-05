module chessopeningtrainer {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires javafx.graphics;
    requires java.desktop;

    opens chessopeningtrainer.view to javafx.fxml;
    exports chessopeningtrainer;
}