module com.tuempresa.papeleriacampus {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.tuempresa.papeleriacampus to javafx.fxml;
    exports com.tuempresa.papeleriacampus;
}