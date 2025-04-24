module com.example.eventmatch_pr {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires TrayNotification;


    opens com.example.eventmatch_pr to javafx.fxml;
    exports com.example.eventmatch_pr;
}