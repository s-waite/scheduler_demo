module dev.sam.scheduler {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens dev.sam.scheduler to javafx.fxml;
    exports dev.sam.scheduler;
    opens dev.sam.scheduler.controller to javafx.fxml;
    exports dev.sam.scheduler.controller;
    opens dev.sam.scheduler.view to javafx.fxml;
    exports dev.sam.scheduler.view;
    opens dev.sam.scheduler.dao to javafx.fxml;
    exports dev.sam.scheduler.dao;
    opens dev.sam.scheduler.model to javafx.fxml;
    exports dev.sam.scheduler.model;
}