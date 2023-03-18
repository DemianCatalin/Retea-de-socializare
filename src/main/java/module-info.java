module com.example.reteadesocializare {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.reteadesocializare to javafx.fxml;
    exports com.example.reteadesocializare;
    opens com.example.reteadesocializare.models to javafx.fxml;
    exports com.example.reteadesocializare.models;
}