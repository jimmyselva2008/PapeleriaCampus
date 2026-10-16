module com.tuempresa.papeleriacampus {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.tuempresa.papeleriacampus to javafx.fxml;
    exports com.tuempresa.papeleriacampus;

    opens com.tuempresa.papeleriacampus.controller to javafx.fxml;
    exports com.tuempresa.papeleriacampus.controller;

    opens com.tuempresa.papeleriacampus.model to javafx.fxml;
    exports com.tuempresa.papeleriacampus.model;
}