module com.example.eventmatch_pr {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires TrayNotification;
    requires mysql.connector.j;
    requires java.mail;
    requires activation;
    requires client;
    requires java.desktop;
    requires com.google.zxing;
    requires jbcrypt;
    requires twilio;


    opens com.example.eventmatch_pr to javafx.fxml;
    exports com.example.eventmatch_pr;
}