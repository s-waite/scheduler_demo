module dev.sam.scheduler {
    requires javafx.controls;
    requires javafx.fxml;


    opens dev.sam.scheduler to javafx.fxml;
    exports dev.sam.scheduler;
    opens dev.sam.scheduler.controller to javafx.fxml;
    exports dev.sam.scheduler.controller;
    opens dev.sam.scheduler.view to javafx.fxml;
    exports dev.sam.scheduler.view;
}