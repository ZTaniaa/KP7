module com.example.kp7 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.kp7 to javafx.fxml;
    exports com.example.kp7;
}