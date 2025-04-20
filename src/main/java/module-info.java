module com.example.eventmatch_pr {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.eventmatch_pr to javafx.fxml;
    exports com.example.eventmatch_pr;
}